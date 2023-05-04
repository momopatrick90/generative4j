package v1.utils;

public class StringUtils {
    public static String subStringCheckbounds(String text, int start, int end) {
        if (start >= end || start >= text.length()) {
            return "";
        }

        if (text.length() < end) {
            end  = text.length();
        }

        if (start < 0) {
            start = 0;
        }

        return text.substring(start, end);
    }
}
