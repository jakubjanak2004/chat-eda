package messaging;

/**
 * RabbitMQ fanout used only for {@code message-created} delivery to {@code ws-service}.
 * Kafka topic {@code message-created} remains for {@code search-service} and other consumers.
 */
public final class MessageCreatedAmqp {

    private MessageCreatedAmqp() {}

    public static final String FANOUT_EXCHANGE_MESSAGE_CREATED = "eda.message.created.fanout";
}
