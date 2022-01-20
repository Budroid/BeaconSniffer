package nl.budroid.BeaconSniffer.domain;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Network Group
 *
 * @author Robert Buddenbohmer
 * @since 0.1
 */
public class NetworkGroup {

    @Getter
    private final String groupName;

    @Getter
    private final Set<WirelessNetwork> networks = new HashSet<>();

    public NetworkGroup(String groupName){
        this.groupName = groupName;
    }

//    private Set<WirelessNetwork> constructNetworks(List<APInterface> interfaceList) {
////        List<WirelessNetwork> networks = new ArrayList<>();
//
//        // All this (unique) visable interfaces are separate access points
//        Set<APInterface> visableInterfaces = interfaceList.stream()
//                .filter(APInterface::isVisible)
//                .collect(Collectors.toSet());
//
//        // Now we split the network group in networks and create access points for the networks
////        Map<String, Set<AccessPoint>> networkMap = new HashMap<>();
//        Map<String, WirelessNetwork> networkMap = new HashMap<>();
//        for (APInterface apInterface : visableInterfaces) {
//            AccessPoint accessPoint = new AccessPoint();
//            accessPoint.getApInterfaces().add(apInterface);
//            if (networkMap.containsKey(apInterface.getSsId())) {
//                networkMap.get(apInterface.getSsId()).getAccessPoints().add(accessPoint);
//            } else {
//                WirelessNetwork network = new WirelessNetwork(apInterface.getSsId());
//                network.getAccessPoints().add(accessPoint);
//                networkMap.put(apInterface.getSsId(), network);
//            }
//        }
//        return new HashSet<>(networkMap.values());
//    }

    private String constructName(List<APInterface> interfaceList) {
        String groupName = interfaceList.stream()
                .filter(APInterface::isVisible)
                .map(APInterface::getSsId)
                .distinct()
                .collect(Collectors.joining(" | "));
        return !groupName.isBlank() ? groupName : "<hidden group>";
    }
}