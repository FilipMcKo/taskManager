package meelogic.filip.taskManager.configurations;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    Logger logger = LoggerFactory.getLogger(SLF4JLogger.class);
    @Bean
    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setHost("10.6.4.172");
        factory.setPort(5672);
        logger.info("RabbitMQ: ConnectionFactory set on 10.6.4.172");
        return factory;
    }
}
