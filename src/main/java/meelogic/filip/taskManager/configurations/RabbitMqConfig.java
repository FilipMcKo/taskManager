package meelogic.filip.taskManager.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;

@Configuration
public class RabbitMqConfig {
    private static final String QUEUE_NAME = "myQueue";
    private static final String FANOUT_EXCHANGE = "myExchange";

    @Bean
    Queue queue() {
        Map<String, Object> args = new HashMap();
        args.put("x-max-priority", 10);
        return new Queue(QUEUE_NAME, true, false,false, args);
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(1);
        container.setConcurrency("1");
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(TestReceiver receiver) throws InterruptedException {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

}
