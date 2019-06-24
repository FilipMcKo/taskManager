package meelogic.filip.taskManager.configurations;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class RabbitMqConfig {
    private static final String QUEUE_NAME = "myQueue";
    private static final String FANOUT_EXCHANGE = "myExchange";

    @Bean
    public List<Declarable> fanoutBindings() {
        Map<String, Object> args = new HashMap();
        args.put("x-max-priority", 10);
        Queue fanoutQueue = new Queue(QUEUE_NAME, true, false, false, args);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE);

        return Arrays.asList(
                fanoutQueue,
                fanoutExchange,
                bind(fanoutQueue).to(fanoutExchange));
    }
/*
    na koniec pamiętać o testach
*/
}
