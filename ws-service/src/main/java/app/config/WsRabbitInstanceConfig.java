package app.config;

import messaging.MessageCreatedAmqp;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WsRabbitInstanceConfig {

    @Bean
    public WsInstanceIdentity wsInstanceIdentity(
            @Value("${eda.ws.instance-id:${HOSTNAME:ws-local}}") String rawInstanceId) {
        return WsInstanceIdentity.fromRaw(rawInstanceId);
    }

    @Bean
    public DirectExchange wsMessageDirectExchange() {
        return new DirectExchange(MessageCreatedAmqp.DIRECT_EXCHANGE_WS, true, false);
    }

    @Bean
    public Queue wsMessageQueue(WsInstanceIdentity id) {
        return new Queue("eda.ws.q." + id.routingKey(), true, false, false);
    }

    @Bean
    public Binding wsMessageBinding(Queue wsMessageQueue, DirectExchange wsMessageDirectExchange, WsInstanceIdentity id) {
        return BindingBuilder.bind(wsMessageQueue).to(wsMessageDirectExchange).with(id.routingKey());
    }
}
