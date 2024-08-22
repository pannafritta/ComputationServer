package server.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ResponseFormatter {

    public static String format(double value) {
        DecimalFormat df = new DecimalFormat("0.0000000", new DecimalFormatSymbols(Locale.US));
        return df.format(value);
    }

    public static String okResponse(double requestTime, String output) {
        return "OK;" + format(requestTime) + ";" + output + System.lineSeparator();
    }

    public static String errResponse(String errorMessage) {
        return "ERR;" + errorMessage + System.lineSeparator();
    }
}
