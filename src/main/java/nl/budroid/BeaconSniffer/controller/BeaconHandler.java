package nl.budroid.BeaconSniffer.controller;

import nl.budroid.BeaconSniffer.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class BeaconHandler {
    private final List<BeaconFrame> allBeacons;

    public BeaconHandler() {
        allBeacons = new ArrayList<>();
    }

    public void add(BeaconFrame beaconFrame) {
        allBeacons.add(beaconFrame);
    }

    public Set<NetworkGroup> getNetworkGroups() {
        Set<BeaconFrame> uniqueBeacons = new HashSet<>(allBeacons);
        // Create a set of all AP interfaces found in the beacons
        Set<APInterface> apInterfaces = uniqueBeacons.stream()
                .map(uniqueBeacon -> new APInterface(uniqueBeacon.getSSID(), uniqueBeacon.getBSSID(), uniqueBeacon.getPower()))
                .collect(Collectors.toSet());

        // Split the interfaces in visible and hidden
        Set<APInterface> visibleAPInterfaces = apInterfaces.stream().filter(APInterface::isVisible).collect(Collectors.toSet());
        Set<APInterface> hiddenAPInterfaces = apInterfaces.stream().filter(apInterface -> !apInterface.isVisible()).collect(Collectors.toSet());

        // Create networks from the visible interfaces
        Map<String, WirelessNetwork> wirelessNetworks = new HashMap<>();
        for (APInterface visibleAPInterface : visibleAPInterfaces) {
            String interfaceName = visibleAPInterface.getSsId();
            // Every unique visibleAPInterface is physical access point
            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setPower(visibleAPInterface.getPower());
            accessPoint.getApInterfaces().add(visibleAPInterface);
            // Add the access point te the network
            if (wirelessNetworks.containsKey(interfaceName)) {
                wirelessNetworks.get(interfaceName).getAccessPoints().add(accessPoint);
            } else {
                WirelessNetwork wirelessNetwork = new WirelessNetwork(interfaceName);
                wirelessNetwork.getAccessPoints().add(accessPoint);
                wirelessNetworks.put(interfaceName, wirelessNetwork);
            }
        }
        // Add the hidden interfaces to the networks
        enrichNetworksWithHiddenInterfaces(hiddenAPInterfaces, wirelessNetworks);
        wirelessNetworks.forEach((key, value) -> System.out.println(key + ": " + value.getAccessPoints() ));

        // Now the network groups kan be created
        Set<NetworkGroup> networkGroups = createNetworkGroups(wirelessNetworks);








        // Split the interfaces in Network Groups, based on bssid and ssid
        List<List<APInterface>> splittedAPInterfaces = new ArrayList<>();
//        outer:
//        for (APInterface apInterface : apInterfaces) {
//            for (List<APInterface> sublist : splittedAPInterfaces) {
//                for (APInterface apInterfaceToCompare : sublist) {
//                    if (apInterface.isInSameGroup(apInterfaceToCompare)) {
//                        sublist.add(apInterface);
//                        continue outer;
//                    }
//                }
//            }
//            // Zit in geen enkele sublist, dus nieuw aanmaken
//            List<APInterface> newSublist = new ArrayList<>();
//            newSublist.add(apInterface);
//            splittedAPInterfaces.add(newSublist);
//        }
//
//        return splittedAPInterfaces.stream()
//                .map(NetworkGroup::new)
//                .collect(Collectors.toSet());

//        // Create set of all access points
//        Set<AccessPoint> accessPoints = new HashSet<>();
//        for (APInterface apInterface : visibleAPInterfaces) {
//
//        }
//
//        return splittedAPInterfaces;

        // Loop over the beaconframes
//        for (BeaconFrame beaconFrame : allBeacons) {


//            String networkId = beaconFrame.getNetworkID();
//            WirelessNetwork wirelessNetwork;
//            if (networks.containsKey(networkId)) {
//                wirelessNetwork = networks.get(networkId);
//                wirelessNetwork.getAccessPoints()
//                        .add(new AccessPoint(beaconFrame.getSSID(), beaconFrame.getBSSID(), beaconFrame.getPower()));
//            } else {
//                wirelessNetwork = new WirelessNetwork();
//                wirelessNetwork.getAccessPoints()
//                        .add(new AccessPoint(beaconFrame.getSSID(), beaconFrame.getBSSID(), beaconFrame.getPower()));
//                networks.put(networkId, wirelessNetwork);
//            }

//        }

        return networkGroups;
    }

    private Set<NetworkGroup> createNetworkGroups(Map<String, WirelessNetwork> networks) {
        Set<NetworkGroup> networkGroups = new HashSet<>();
        for(WirelessNetwork wirelessNetwork : networks.values()){

        }
        return null;
    }


    private void enrichNetworksWithHiddenInterfaces(Set<APInterface> hiddenAPInterfaces, Map<String, WirelessNetwork> wirelessNetworks) {
        hiddenAPInterfaces:
        for (APInterface hiddenInterface : hiddenAPInterfaces){
            for (WirelessNetwork wirelessNetwork: wirelessNetworks.values()){
                 for(AccessPoint accessPoint : wirelessNetwork.getAccessPoints()){
                     for(APInterface apInterface : accessPoint.getApInterfaces()){
                         if(apInterface.getMatchCount(hiddenInterface) == 11){
                             accessPoint.getApInterfaces().add(hiddenInterface);
                             continue hiddenAPInterfaces;
                         }
                     }
                 }
            }
            System.out.println("Unidentified interface: " + hiddenInterface);
        }
    }

    public int beaconCount() {
        return allBeacons.size();
    }

//    public static BeaconManager getInstance() {
//        if (INSTANCE == null) {
//            synchronized (BeaconManager.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new BeaconManager();
//                }
//            }
//        }
//        return INSTANCE;
//    }
}
