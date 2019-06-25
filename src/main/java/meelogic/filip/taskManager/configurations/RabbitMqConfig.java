package meelogic.filip.taskManager.configurations;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    private static final String QUEUE_NAME = "myQueue";
    private static final String FANOUT_EXCHANGE = "myExchange";

    @Bean
    public String getQueueName(){
        return QUEUE_NAME;
    }

    @Bean
    public String getFanoutExchange(){
        return FANOUT_EXCHANGE;
    }

    @Bean
    public Channel getChannel() {
        Map<String, Object> args = new HashMap();
        args.put("x-max-priority", 10);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Channel channel = null;
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, args);
            channel.exchangeDeclare(FANOUT_EXCHANGE, "fanout");
            channel.queueBind(QUEUE_NAME, FANOUT_EXCHANGE, "");
            channel.basicQos(1, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

}
