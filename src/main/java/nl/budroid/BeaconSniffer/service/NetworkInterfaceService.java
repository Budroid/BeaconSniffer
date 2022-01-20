package nl.budroid.BeaconSniffer.service;

import nl.budroid.BeaconSniffer.domain.AvailableNIF;
import nl.budroid.BeaconSniffer.domain.InterfaceMode;
import nl.budroid.BeaconSniffer.util.CommandLineRunner;
import nl.budroid.BeaconSniffer.util.CommandResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class NetworkInterfaceService {

    public List<AvailableNIF> getAvailableNetworkInterfaces() throws IOException {
        List<AvailableNIF> availableNIFs = new ArrayList<>();
        List<String> outputAsList = CommandLineRunner.run("cmd.exe", "/c", "ipconfig", "/all");
        boolean adapterFound = false;
        int lineToSkip = -1;
        AvailableNIF networkInterface = null;
        for (int i = 0; i < outputAsList.size(); i++) {
            String line = outputAsList.get(i);
            if (i != lineToSkip) {
                if (line.contains("Wireless LAN adapter") && !line.contains("LAN-verbinding")) {
                    // Begin adapter lines
                    adapterFound = true;
                    lineToSkip = i + 1;
                    networkInterface = new AvailableNIF();
                    networkInterface.setExternalName(extractExternalName(line));
                } else if (adapterFound && line.contains("Description")) {
                    networkInterface.setDescription(extractValue(line));
                } else if (adapterFound && "".equals(line)) {
                    // Einde adapter lines
                    adapterFound = false;
                    networkInterface.setMode(getMode(networkInterface.getExternalName()));
                    networkInterface.setInternalName(getInternalName(networkInterface.getDescription()));
                    availableNIFs.add(networkInterface);
                    networkInterface = null;
                }
            }
        }
        return availableNIFs;
    }

    private String getInternalName(String description) throws SocketException {
        Stream<NetworkInterface> networkInterfaceStream = NetworkInterface.networkInterfaces();
        NetworkInterface networkInterface = networkInterfaceStream.filter(nif -> description.equals(nif.getDisplayName()))
                .findAny()
                .orElse(null);
        return networkInterface != null ? networkInterface.getName() : "Unknown";

    }

    private InterfaceMode getMode(String name) throws IOException {
        CommandResult mode = CommandLineRunner.run("cmd.exe", "/c", "C:\\Windows\\System32\\Npcap\\wlanhelper", name, "mode");
        return InterfaceMode.getMode(mode.get(0));
    }

    private static String extractExternalName(String line) {
        return line.substring(21, line.length() - 1);
    }

    private static String extractValue(String line) {
        return line.split(":")[1].trim();
    }

}
