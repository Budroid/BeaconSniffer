package nl.budroid.BeaconSniffer.controller;

import nl.budroid.BeaconSniffer.domain.APInterface;
import nl.budroid.BeaconSniffer.domain.BeaconFrame;
import nl.budroid.BeaconSniffer.domain.NetworkGroup;
import nl.budroid.BeaconSniffer.domain.WirelessNetwork;
import nl.budroid.BeaconSniffer.service.PacketService;
import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Dot11ProbeRequestPacket;
import org.pcap4j.packet.RadiotapPacket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

@RestController
@RequestMapping("/scan")
public class ScanController {

    private static final int SNAPLEN = 65536;
    private static final int FRAMECOUNT = 1;
    private static final String BEACON_FILTER = "wlan[0] == 0x80";
    private static final String PROBE_RESPONSE_FILTER = "wlan[0] == 0x50";
    private static final String NIF_SITECOM = "Sitecom";
    private static final String NIF_TPLINK = "TP-LINK";
    private static final String CURRENT_NIF = NIF_TPLINK;

    private static final int COUNT = 1;

    final PacketService packetService;

    public ScanController(PacketService packetService) {
        this.packetService = packetService;
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, WirelessNetwork>> scanActive() throws PcapNativeException, NotOpenException {
        // Select network interface
        PcapNetworkInterface pcapNetworkInterface = getPcapNetworkInterface();
        // Not found when no wireless interface is available
        if (pcapNetworkInterface == null) {
            return ResponseEntity.notFound().build();
        }


        final PcapHandle sendHandle = new PcapHandle.Builder(pcapNetworkInterface.getName()).snaplen(SNAPLEN)
                .timeoutMillis(1000)
                .rfmon(true)
                .build();

        final PcapHandle listenHandle = new PcapHandle.Builder(pcapNetworkInterface.getName()).snaplen(SNAPLEN)
                .timeoutMillis(1000)
                .rfmon(true)
                .build();


            listenHandle.setFilter(PROBE_RESPONSE_FILTER, BpfProgram.BpfCompileMode.OPTIMIZE);
            ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            PacketListener listener =
                    packet -> {
                        if (packet instanceof RadiotapPacket) {
                            BeaconFrame beaconFrame = new BeaconFrame(packet);
                            System.out.println(beaconFrame.getSSID());
                        }
                    };
            Task t = new Task(listenHandle, listener);
            pool.execute(t);

            Dot11ProbeRequestPacket nullProbeRequest = packetService.getNullProbeRequest();
            sendHandle.sendPacket(nullProbeRequest);

        } finally {
            if (listenHandle != null && listenHandle.isOpen()) {
                listenHandle.close();
            }
            if (sendHandle != null && sendHandle.isOpen()) {
                sendHandle.close();
            }
            if (pool != null && !pool.isShutdown()) {
                pool.shutdown();
            }

            System.out.println("Done");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/passive")
    public ResponseEntity<Set<NetworkGroup>> scanPassive() throws NotOpenException, PcapNativeException {
        // Select network interface
        PcapNetworkInterface pcapNetworkInterface = getPcapNetworkInterface();
        // Not found when no wireless interface is available
        if (pcapNetworkInterface == null) {
            return ResponseEntity.notFound().build();
        }

        final PcapHandle handle = getPcapHandle(pcapNetworkInterface, BEACON_FILTER);

        BeaconHandler beaconHandler = new BeaconHandler();
        PacketListener listener =
                packet -> {
                    if (packet instanceof RadiotapPacket) {
                        BeaconFrame beaconFrame = new BeaconFrame(packet);
                        beaconHandler.add(beaconFrame);
                    }
                };
        try {
            handle.loop(FRAMECOUNT, listener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            handle.close();
        }
        return ResponseEntity.ok(beaconHandler.getNetworkGroups());
    }

    private PcapNetworkInterface getPcapNetworkInterface() {
        PcapNetworkInterface pcapNetworkInterface = null;
        Predicate<PcapNetworkInterface> nifFilter = dev -> dev.getDescription()
                .contains(CURRENT_NIF);
        try {
            pcapNetworkInterface = Pcaps.findAllDevs()
                    .parallelStream()
                    .filter(nifFilter)
                    .findFirst()
                    .orElse(null);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
        return pcapNetworkInterface;
    }

    private PcapHandle getPcapHandle(PcapNetworkInterface pcapNetworkInterface, String filter) throws PcapNativeException, NotOpenException {
        final PcapHandle handle = new PcapHandle.Builder(pcapNetworkInterface.getName()).snaplen(SNAPLEN)
                .promiscuousMode(PromiscuousMode.PROMISCUOUS)
                .timeoutMillis(1000)
                .rfmon(true)
                .build();
        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        return handle;
    }

    private static class Task implements Runnable {

        private PcapHandle handle;
        private PacketListener listener;

        public Task(PcapHandle handle, PacketListener listener) {
            this.handle = handle;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                handle.loop(10, listener);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        }
    }



}
