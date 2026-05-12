package messaging;

/**
 * RabbitMQ direct exchange for {@code message-created} → {@code ws-service}.
 * Routing key = sanitized {@code ws-service} instance id (see {@link WsUserPresenceKeys}).
 * Kafka topic {@code message-created} remains for {@code search-service}.
 */
public final class MessageCreatedAmqp {

    private MessageCreatedAmqp() {}

    /** Fanout exchange (legacy); no longer used when routed delivery is enabled. */
    public static final String FANOUT_EXCHANGE_MESSAGE_CREATED = "eda.message.created.fanout";

    /** Direct exchange: publish with routing key = ws instance id. */
    public static final String DIRECT_EXCHANGE_WS = "eda.ws.message";
}
