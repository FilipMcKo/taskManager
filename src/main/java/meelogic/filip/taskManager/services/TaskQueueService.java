package meelogic.filip.taskManager.services;

import com.rabbitmq.client.*;
import meelogic.filip.taskManager.entities.internal.Task;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class TaskQueueService {

    private final static String QUEUE_NAME = "Finished tasks";
    Logger logger = LoggerFactory.getLogger(SLF4JLogger.class);
/*
    @Autowired
    ConnectionFactory factory;*/

    void publishToQueue(Task task) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.6.4.172");
        //factory.setPort(15672);
        factory.setUsername("root");
        factory.setPassword("root");
        logger.info("RabbitMQ: ConnectionFactory set on 10.6.4.172:15672");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            logger.info("RabbitMQ: queque created");
            channel.basicPublish("", QUEUE_NAME, null, task.toString().getBytes());
            channel.close();
            logger.info("RabbitMQ: channel closed");
            connection.close();
            logger.info("RabbitMQ: connection closed");
            //TODO: Task powinien być serializowalny, żebym mógł go prawidłowo podawać do kolejki
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
