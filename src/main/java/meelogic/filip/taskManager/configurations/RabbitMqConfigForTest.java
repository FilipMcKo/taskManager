package meelogic.filip.taskmanager.configurations;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Profile("test")
@Configuration
public class RabbitMqConfigForTest {

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
    private static final String QUEUE_NAME = "myQueueTest";
    private static final String FANOUT_EXCHANGE = "myExchangeTest";

    @Bean
    public String getQueueName() {
        return QUEUE_NAME;
    }

    @Bean
    public String getFanoutExchange() {
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
            LOGGER.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
        return channel;
    }
}
