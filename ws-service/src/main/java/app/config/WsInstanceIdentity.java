package app.config;

import messaging.WsUserPresenceKeys;

/**
 * Sanitized id for this JVM: Redis presence + Rabbit routing key + queue suffix must match.
 */
public record WsInstanceIdentity(String routingKey) {
    public static WsInstanceIdentity fromRaw(String raw) {
        return new WsInstanceIdentity(WsUserPresenceKeys.sanitizeInstanceId(raw));
    }
}
