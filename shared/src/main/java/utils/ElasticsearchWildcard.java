package utils;

/**
 * Escapes user input for Elasticsearch {@code wildcard} query {@code value}
 * so {@code *} and {@code ?} in the text are treated literally.
 */
public final class ElasticsearchWildcard {
    private ElasticsearchWildcard() {
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
