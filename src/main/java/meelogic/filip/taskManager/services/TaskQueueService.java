package meelogic.filip.taskManager.services;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TaskQueueService {
    private final RabbitTemplate rabbitTemplate;

    public TaskQueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessage(Integer id) {
        this.rabbitTemplate.convertAndSend("myExchange", "", id.toString(), message -> {
            message.getMessageProperties().setPriority(id % 3);
            return message;
        });
    }

}
