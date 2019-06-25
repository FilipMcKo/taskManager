package meelogic.filip.taskManager.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;

import static meelogic.filip.taskManager.entities.internal.State.*;
import static meelogic.filip.taskManager.services.exceptions.OperationStatus.*;

@Service
public class TaskStateService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Channel channel;

    @Qualifier("getFanoutExchange")
    @Autowired
    private String fanoutExchange;

    @Qualifier("getQueueName")
    @Autowired
    private String queue;

    public void putTaskOnQueue(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == NEW, FORBIDDEN_OPERATION);
        optTask.ifPresent(task -> {
            try {
                putTaskOnQueue(task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void putTaskOnQueue(Task task) throws IOException {
        task.setCurrentState(PENDING);
        taskRepository.save(task);
        AMQP.BasicProperties.Builder basicProps = new AMQP.BasicProperties.Builder();
        basicProps.priority(task.getPriority().getPriorityAsInteger());
        channel.basicPublish(fanoutExchange, queue, basicProps.build(), BigInteger.valueOf(task.getId()).toByteArray());
    }


    public void cancelProcessingTask(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == RUNNING || optTask.get().getCurrentState() == PENDING, FORBIDDEN_OPERATION);
        optTask.ifPresent(this::cancelProcessingTask);
    }

    private void cancelProcessingTask(Task task) {
        task.setCurrentState(CANCELLED);
        task.setProgressPercentage(0.0);
        task.setTaskBeginTime(null);
        taskRepository.save(task);
    }
}
