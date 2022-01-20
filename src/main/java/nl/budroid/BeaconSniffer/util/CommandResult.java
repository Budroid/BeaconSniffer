package nl.budroid.BeaconSniffer.util;

import java.util.ArrayList;
import java.util.List;

public class CommandResult extends ArrayList<String> {

    private static final String NEWLINE = System.getProperty("line.separator");

//    TODO Zonder extra functionalitieit slaat deze klasse nergens meer op, maar is het slechts een gewone ArrayList.

    public CommandResult(List<String> rawResult) {
        super();
        this.addAll(rawResult);
    }

//    public String toFormattedString() throws IOException {
//        StringBuilder result = new StringBuilder(80);
//        while (true) {
//            String line = this.readLine();
//            if (line == null)
//                break;
//            result.append(line).append(NEWLINE);
//        }
//        return  result.toString();
//    }

}
