package nl.budroid.BeaconSniffer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.IntStream;

@AllArgsConstructor
public class APInterface {
    @Getter
    private String ssId;

    @Getter
    private String bssId;

    @Getter
    private int power;

    public boolean isVisible(){
       return !"<hidden>".equals(this.getSsId());
    }

    public boolean isInSameGroup(APInterface apInterface) {
        return (this.isVisible() && apInterface.isVisible() && this.getSsId()
                .equals(apInterface.getSsId())) || getMatchCount(apInterface) > 5;
    }

    public int getMatchCount(APInterface apInterface) {
        return (int) IntStream.range(0, 12)
                .filter(i -> this.bssId.charAt(i) == apInterface.getBssId().charAt(i))
                .count();
    }

    @Override
    public String toString() {
        return "APInterface{" +
                "ssId='" + ssId + '\'' +
                ", bssId='" + bssId + '\'' +
                '}';
    }
}
