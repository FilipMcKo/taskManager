package meelogic.filip.taskManager.services;

import com.rabbitmq.client.*;
import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class TaskQueueService {

    private final static String QUEUE_NAME = "Finished tasks";

    @Autowired
    ConnectionFactory factory;

    public void publishToQueue(Task task) {
        try (Connection connection = factory.newConnection()
            ) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, task.toString().getBytes());
            channel.close();
            //TODO: Task powinien być serializowalny, żebym mógł go prawidłowo podawać do kolejki
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
