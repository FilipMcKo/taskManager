package meelogic.filip.taskManager.services;

import com.rabbitmq.client.*;
import meelogic.filip.taskManager.entities.internal.Task;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

@Service
public class TaskQueueService {

    private final static String QUEUE_NAME = "Finished tasks";
    Logger logger = LoggerFactory.getLogger(SLF4JLogger.class);

    @Autowired
    ConnectionFactory factory;

    void publishToQueue(Task task) {
        try {
            Connection connection = factory.newConnection();
            logger.info("RabbitMQ: connection opened");
            Channel channel = connection.createChannel();
            logger.info("RabbitMQ: channel opened");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            logger.info("RabbitMQ: queue declared");
            channel.basicPublish("", QUEUE_NAME, null, this.taskToByteArray(task));
            logger.info("RabbitMQ: object added to queue");
            channel.close();
            logger.info("RabbitMQ: channel closed");
            connection.close();
            logger.info("RabbitMQ: connection closed");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    byte[] taskToByteArray(Task task) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(task);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
}
