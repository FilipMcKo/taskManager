package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessing(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), HttpStatus.NOT_FOUND);
        Task task = optTask.get();
        Preconditions.checkArgument(task.getCurrentState() == State.NEW, HttpStatus.FORBIDDEN);

        task.setTaskBeginTime(Instant.now().toEpochMilli());
        task.setCurrentState(State.RUNNING);
        task.setNotRunning(false);
        taskRepository.save(task);
    }

    public void cancelProcessing(Integer id) {
        Optional<Task> optTask = taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), HttpStatus.NOT_FOUND);
        Task task = optTask.get();
        Preconditions.checkArgument(task.getCurrentState() == State.RUNNING, HttpStatus.FORBIDDEN);

        optTask.get().setCurrentState(State.CANCELLED);
        optTask.get().setProgressPercentage(0.0);
        optTask.get().setTaskBeginTime(null);
        optTask.get().setNotRunning(true);
        taskRepository.save(task);
    }
}
