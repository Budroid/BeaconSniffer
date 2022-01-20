package nl.budroid.BeaconSniffer.service;

import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.Dot11FrameType;
import org.pcap4j.util.MacAddress;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PacketService {
    private static final MacAddress SRC_MAC_ADDR = MacAddress.getByName("fe:00:01:02:03:04");

    public Dot11ProbeRequestPacket getNullProbeRequest() {

        Dot11ProbeRequestPacket.Builder nullProbebuilder = new Dot11ProbeRequestPacket.Builder();

        Dot11FrameControl.Builder frameContBuilder = new Dot11FrameControl.Builder();
        frameContBuilder.type(Dot11FrameType.PROBE_REQUEST).protocolVersion(Dot11FrameControl.ProtocolVersion.V0);

        Dot11SsidElement.Builder builder = new Dot11SsidElement.Builder();

        Dot11SequenceControl.Builder sequenceControlBuilder = new Dot11SequenceControl.Builder();
        sequenceControlBuilder.sequenceNumber((short) 1);

        Dot11SupportedRatesElement.Builder supportedRatesBuilder = new Dot11SupportedRatesElement.Builder();
        List<Dot11AbstractSupportedRatesElement.Datum> supportedRates = Stream.of((byte) 0x02, (byte) 0x04, (byte) 0x0B, (byte) 0x0C, (byte) 0x12, (byte) 0x16, (byte) 0x18, (byte) 0x24)
                .map(rate -> new Dot11AbstractSupportedRatesElement.Rate(true, rate))
                .collect(Collectors.toList());
        supportedRatesBuilder.ratesAndBssMembershipSelectors(supportedRates);
        supportedRatesBuilder.correctLengthAtBuild(true);

        nullProbebuilder.frameControl(frameContBuilder.build())
                .address1(MacAddress.ETHER_BROADCAST_ADDRESS)
                .address2(SRC_MAC_ADDR)
                .address3(MacAddress.ETHER_BROADCAST_ADDRESS)
                .sequenceControl(sequenceControlBuilder.build())
                .ssid(builder.ssid("Rumah Senang Gast").build())
                .supportedRates(supportedRatesBuilder.build());

        return nullProbebuilder.build();
    }

}
