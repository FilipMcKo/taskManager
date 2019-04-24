package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.services.exceptions.*;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;

@Service
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessing(Integer id) {
        //TODO: obsłużyć optionale i obiekty optionalowe nazywac optTask a nie task
        //Optional<Task> optTask = taskRepository.findById(id);

        // TODO add preconditions
        //This method accepts a boolean condition and throws an IllegalArgumentException when the condition is false.
         //Preconditions.checkArgument(optTask.isPresent(), "Wypierdalaj!");
        // Preconditions.checkArgument(optTask.get().getCurrentState() == State.NEW, "Wypierdalaj!");


        Optional<Task> task;
        try {
            task = taskRepository.findById(id);
        } catch (EntityNotFoundException e) {
            // TODO: exceptions out
            throw new EntityDoesNotExistException();
        }
        if (task.get().getCurrentState().equals(State.RUNNING)) {
            throw new TaskIsAlreadyRunningException();
        }
        if (task.get().getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        }
        task.get().setTaskBeginTime(Instant.now().toEpochMilli());
        task.get().setCurrentState(State.RUNNING);
        task.get().setNotRunning(false);
        taskRepository.save(task.get());
    }

    public void cancelProcessing(Integer id) {
        Optional<Task> task;
        try {
            task = taskRepository.findById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
        if (task.get().getCurrentState().equals(State.RUNNING)) {
            task.get().setCurrentState(State.CANCELLED);
            task.get().setProgressPercentage(0.0);
            task.get().setTaskBeginTime(null);
            task.get().setNotRunning(true);
            taskRepository.save(task.get());
        } else if (task.get().getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        } else if (task.get().getCurrentState().equals(State.CANCELLED)) {
            throw new TaskWasAlreadyCancelledException();
        } else if (task.get().getCurrentState().equals(State.NEW)) {
            throw new TaskWasNeverStartedException();
        }
    }
}
