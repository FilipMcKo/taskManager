package meelogic.filip.taskManager.services;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TaskQueueService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Integer taskId, Integer priority) {
        this.rabbitTemplate.convertAndSend("myExchange", "", taskId.toString(), message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        });
    }

}
