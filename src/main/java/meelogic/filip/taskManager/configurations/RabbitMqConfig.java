package meelogic.filip.taskManager.configurations;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;

public class RabbitMqConfig {
    private final static String QUEUE_NAME = "Finished tasks";

    @Bean
    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.6.4.172");
        factory.setPort(15672);
        return factory;
    }
}
