package nl.budroid.BeaconSniffer.domain;

import lombok.Getter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WirelessNetwork {

    @Getter
    private String name;
    @Getter
    private Set<AccessPoint> accessPoints;

    public WirelessNetwork(String name){
        this.name = name;
        this.accessPoints = new HashSet<>();
    }

    @Override
    public String toString() {
        return "WirelessNetwork{" +
                "accessPoints=" + accessPoints +
                '}';
    }
}
