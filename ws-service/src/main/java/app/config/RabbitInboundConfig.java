package app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitInboundConfig {

    /**
     * Spring AMQP's {@link Jackson2JsonMessageConverter} is built on FasterXML Jackson 2
     * ({@code com.fasterxml.jackson.databind}). Spring Boot 4's default JSON stack is Jackson 3
     * ({@code tools.jackson}), so we use a dedicated FasterXML {@link ObjectMapper} here.
     */
    @Bean
    public Jackson2JsonMessageConverter rabbitJacksonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages(
                "dto.event",
                "dto.response",
                "dto.request",
                "enumeration",
                "java.util",
                "java.time",
                "java.lang"
        );
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter rabbitJacksonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(rabbitJacksonMessageConverter);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
