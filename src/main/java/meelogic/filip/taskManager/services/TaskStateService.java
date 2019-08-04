package meelogic.filip.taskmanager.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import javassist.bytecode.stackmap.TypeData.ClassName;
import meelogic.filip.taskmanager.services.repository.TaskRepository;
import meelogic.filip.taskmanager.entities.internal.Task;
import meelogic.filip.taskmanager.services.exceptions.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static meelogic.filip.taskmanager.entities.internal.State.*;
import static meelogic.filip.taskmanager.services.exceptions.OperationStatus.*;

@Service
public class TaskStateService {

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());

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
                LOGGER.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
            }
        });
    }

    private void putTaskOnQueue(Task task) throws IOException {
        task.setCurrentState(PENDING);
        taskRepository.save(task);
        AMQP.BasicProperties.Builder basicProps = new AMQP.BasicProperties.Builder();
        basicProps.contentType("text/plain").priority(task.getPriority());
        byte[] message = task.getId().toString().getBytes();
        channel.basicPublish(fanoutExchange, queue, basicProps.build(), message);
    }


    public void cancelProcessingTask(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
        Preconditions
            .checkArgument(optTask.get().getCurrentState() == RUNNING || optTask.get().getCurrentState() == PENDING,
                FORBIDDEN_OPERATION);
        optTask.ifPresent(this::cancelProcessingTask);
    }

    private void cancelProcessingTask(Task task) {
        task.setCurrentState(CANCELLED);
        task.setProgressPercentage(0.0);
        task.setTaskBeginTime(null);
        taskRepository.save(task);
    }
}
