package nl.budroid.BeaconSniffer.domain;

import lombok.Getter;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.RadiotapPacket;
import org.pcap4j.util.ByteArrays;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BeaconFrame {

    private static final int BEACON_INFO_LENGTH = 24;
    private static final int FIXED_PARAMETER_LENGTH = 12;

    @Getter
    private final byte[] payload;
    @Getter
    private final byte power;
    @Getter
    private final String BSSID;
    @Getter
    private final String SSID;

    public BeaconFrame(Packet packet) {

        RadiotapPacket.RadiotapHeader radioTapHeader = (RadiotapPacket.RadiotapHeader) packet.getHeader();
        ArrayList<RadiotapPacket.RadiotapData> dataFields = radioTapHeader.getDataFields();

        dataFields.forEach(field -> {
            System.out.println("FIELD: ");
            System.out.println(field.toString());});


        this.payload = packet.getPayload()
                .getRawData();
        this.power = packet.getRawData()[22];
        this.SSID = extractSSID();
        this.BSSID = extractBSSID();
    }

    private String extractBSSID() {
        return ByteArrays.toHexString(Arrays.copyOfRange(payload, 16, 16 + 6), "");
    }

    private String extractSSID() {
        int offset = BEACON_INFO_LENGTH + FIXED_PARAMETER_LENGTH;
        byte tagLength = payload[offset + 1];
        byte[] ssidBytes = Arrays.copyOfRange(payload, offset + 2, offset + 2 + tagLength);
        String ssid = new String(ssidBytes);
        if (ssid.isBlank() || ssid.matches("\u0000+")) ssid = "<hidden>";
        return ssid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeaconFrame)) return false;
        BeaconFrame that = (BeaconFrame) o;
        return getBSSID().equals(that.getBSSID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBSSID());
    }
}
