package messaging;

/**
 * Redis keys for mapping an online username to the {@code ws-service} instance that holds its STOMP session.
 */
public final class WsUserPresenceKeys {

    private WsUserPresenceKeys() {}

    public static String userToInstance(String username) {
        return "eda:ws:user:" + username;
    }

    /** Normalize instance id for Rabbit routing keys and queue names (alphanumeric, dot, dash, underscore). */
    public static String sanitizeInstanceId(String raw) {
        if (raw == null || raw.isBlank()) {
            return "unknown";
        }
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
