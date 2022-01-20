package nl.budroid.BeaconSniffer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class AccessPoint {

    @Setter
    @Getter
    private int power;

    @Setter
    @Getter
    private long timestamp;

    @Getter
    private final List<APInterface> apInterfaces = new ArrayList<>();


//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof AccessPoint){
//            AccessPoint ap1 = (AccessPoint) obj;
//            AccessPoint ap2 = this;
//            return ap1.getMacAddress().equals(ap2.macAddress);
//        }else{
//            return false;
//        }
//    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof AccessPoint)) return false;
//        AccessPoint that = (AccessPoint) o;
//        return Objects.equals(getSsId(), that.getSsId()) && Objects.equals(getBssId(), that.getBssId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getSsId(), getBssId());
//    }

    @Override
    public String toString() {
        return "AccessPoint{power= " + power + ", interfaces='" + apInterfaces + '\'' +'}';
    }

}