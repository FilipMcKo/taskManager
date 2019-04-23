package meelogic.filip.taskManager.services;

import com.google.common.base.Preconditions;
import meelogic.filip.taskManager.services.exceptions.*;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

@Service
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessing(Integer id) {
        //Optional<Task> optTask = taskRepository.findById(id);

        // TODO add preconditions
        //This method accepts a boolean condition and throws an IllegalArgumentException when the condition is false.
         Preconditions.checkArgument(optTask.isPresent(), "Wypierdalaj!");
        // Preconditions.checkArgument(optTask.get().getCurrentState() == State.NEW, "Wypierdalaj!");


        Task task;
        try {
            task = taskRepository.read(id);
        } catch (EntityNotFoundException e) {
            // TODO: exceptions out
            throw new EntityDoesNotExistException();
        }
        if (task.getCurrentState().equals(State.RUNNING)) {
            throw new TaskIsAlreadyRunningException();
        }
        if (task.getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        }
        task.setTaskBeginTime(Instant.now().toEpochMilli());
        task.setCurrentState(State.RUNNING);
        task.setNotRunning(false);
        taskRepository.update(task);
    }

    public void cancelProcessing(Integer id) {
        Task task;
        try {
            task = taskRepository.read(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
        if (task.getCurrentState().equals(State.RUNNING)) {
            task.setCurrentState(State.CANCELLED);
            task.setProgressPercentage(0.0);
            task.setTaskBeginTime(null);
            task.setNotRunning(true);
            taskRepository.update(task);
        } else if (task.getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        } else if (task.getCurrentState().equals(State.CANCELLED)) {
            throw new TaskWasAlreadyCancelledException();
        } else if (task.getCurrentState().equals(State.NEW)) {
            throw new TaskWasNeverStartedException();
        }
    }
}
