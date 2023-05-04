package v1.utils;

public class StringUtils {
    public static String subStringCheckbounds(String text, Integer end) {
        if (end == null) {
            return "";
        }

        if (text.length() < end) {
            return "";
        }

        return text.substring(0, end.intValue());
    }
}
