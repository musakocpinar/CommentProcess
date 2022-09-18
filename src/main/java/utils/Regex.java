package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static Matcher get(String regex, String body) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        boolean matchFound = matcher.find();
        if (!matchFound){
            return null;
        }

        return matcher;
    }

    public static boolean isMatch(String regex, String body){
        Matcher matcher = get(regex, body);
        return matcher != null && matcher.matches();
    }
}
