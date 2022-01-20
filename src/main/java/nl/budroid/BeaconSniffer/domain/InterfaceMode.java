package nl.budroid.BeaconSniffer.domain;

public enum InterfaceMode {
    MANAGED,MONITOR,UNKNOWN;

    public static InterfaceMode getMode(String mode){
        InterfaceMode result = InterfaceMode.UNKNOWN;
        if("managed".equals(mode)){
            result = InterfaceMode.MANAGED;
        }else if("monitor".equals(mode)){
            result = InterfaceMode.MONITOR;
        }
        return result;
    }
}
