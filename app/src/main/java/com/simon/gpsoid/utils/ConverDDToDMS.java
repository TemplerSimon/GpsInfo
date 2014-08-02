package com.simon.gpsoid.utils;

/**
 * Created by simon on 30.06.14.
 */
public class ConverDDToDMS {

    private static final double hour = 60;
    private static final String DOT = "\\.";

    public static String getDMS(String ddCord) {
        String[] splitString;
        String degrees;
        String minutes;
        String seconds;
        String[] ms;

        if ("".equals(ddCord)) {
            return "";
        }

        splitString = ddCord.split(DOT);
        degrees = splitString[0];

        ms = Double
                .toString((Double.parseDouble("0." + splitString[1]) * hour))
                .split(DOT);

        minutes = ms[0];

        seconds = Double.toString((Double.parseDouble("0." + ms[1]) * hour))
                .split(DOT)[0];

        return degrees + " " + minutes + "' " + seconds + "\"";

    }
}
