package nl.budroid.BeaconSniffer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AvailableNIF {
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private InterfaceMode mode;
    @Getter
    @Setter
    private String internalName;
    @Getter
    @Setter
    private String externalName;
}
