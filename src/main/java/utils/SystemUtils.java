package utils;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Log4j2
public class SystemUtils {
    private static final NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);

    private SystemUtils() {}

    @SneakyThrows(InterruptedException.class)
    public static void sleep (int milis){
        Thread.sleep(milis);
    }

    /**
     * Converter
     * Ex: 1.4k -> 1400
     * */
    public static Number compactNumberParse (String val) throws ParseException {
        return fmt.parse(val);
    }
}
