package app.utils;

import utils.ElasticsearchWildcard;

public class Elasticsearch {
    public static String toContentWildcardPattern(String trimmedQuery) {
        return "*" + ElasticsearchWildcard.escape(trimmedQuery) + "*";
    }
}
