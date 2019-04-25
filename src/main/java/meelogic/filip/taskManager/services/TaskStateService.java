package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.services.exceptions.OperationStatus;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessingTask(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), OperationStatus.ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == State.NEW, OperationStatus.FORBIDDEN_OPERATION);
        optTask.ifPresent(this::startProcessingTask);
    }

    private void startProcessingTask(Task task) {
        task.setTaskBeginTime(Instant.now().toEpochMilli());
        task.setCurrentState(State.RUNNING);
        taskRepository.save(task);
    }

    public void cancelProcessingTask(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), OperationStatus.ENTITY_NOT_FOUND);
        Preconditions.checkArgument(optTask.get().getCurrentState() == State.RUNNING, OperationStatus.FORBIDDEN_OPERATION);
        optTask.ifPresent(this::cancelProcessingTask);
    }

    private void cancelProcessingTask(Task task) {
        task.setCurrentState(State.CANCELLED);
        task.setProgressPercentage(0.0);
        task.setTaskBeginTime(null);
        taskRepository.save(task);
    }
}
