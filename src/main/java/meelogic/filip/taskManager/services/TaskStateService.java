package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static meelogic.filip.taskManager.entities.internal.State.*;
import static meelogic.filip.taskManager.services.exceptions.OperationStatus.*;

@Service
public class TaskStateService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskQueueService taskQueueService;

    public void putTaskOnQueue(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == NEW, FORBIDDEN_OPERATION);
        optTask.ifPresent(this::putTaskOnQueue);
    }

    private void putTaskOnQueue(Task task) {
        //task.setTaskBeginTime(Instant.now().toEpochMilli());
        task.setCurrentState(PENDING);
        taskRepository.save(task);
        taskQueueService.sendMessage(task.getId(), task.getPriority().getPriorityAsInteger());
    }

    public void startProcessingTask(Task task){
        task.setTaskBeginTime(Instant.now().toEpochMilli());
        task.setCurrentState(RUNNING);
        taskRepository.save(task);
    }

    public void cancelProcessingTask(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == RUNNING || optTask.get().getCurrentState() == PENDING, FORBIDDEN_OPERATION);
        optTask.ifPresent(this::cancelProcessingTask);
    }

    private void cancelProcessingTask(Task task) {
        task.setCurrentState(State.CANCELLED);
        task.setProgressPercentage(0.0);
        task.setTaskBeginTime(null);
        taskRepository.save(task);
    }
}
