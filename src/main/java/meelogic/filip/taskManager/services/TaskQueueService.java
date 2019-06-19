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


//    public void publishMessage() {
//        this.rabbitTemplate.convertAndSend("","message from taskManager");
//    }

    public void publishMessage() {
        this.rabbitTemplate.convertAndSend("myExchange","","message from taskManager", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setPriority(1);
                return message;
            }
        });
    }

}
