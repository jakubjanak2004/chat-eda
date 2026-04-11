package app.utils;

public class Elasticsearch {
    public static String toContentWildcardPattern(String trimmedQuery) {
        return "*" + escape(trimmedQuery) + "*";
    }

    public static String escape(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("?", "\\?");
    }
}
