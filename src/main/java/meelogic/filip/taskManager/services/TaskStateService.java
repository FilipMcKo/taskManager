package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.services.exceptions.*;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessing(Integer id) {
        Task task;
        try {
            task = taskRepository.read(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
        if (task.getCurrentState().equals(State.RUNNING)) {
            throw new TaskIsAlreadyRunningException();
        }
        if (task.getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        }
        task.setTaskBeginTime(System.currentTimeMillis());
        task.setCurrentState(State.RUNNING);
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
            taskRepository.update(task);
        } else if (task.getCurrentState().equals(State.FINISHED)) {
            throw new TaskIsAlreadyFinishedException();
        } else if (task.getCurrentState().equals(State.CANCELLED)) {
            throw new TaskWasAlreadyCancelledException();
        } else if (task.getCurrentState().equals(State.NONE)) {
            throw new TaskWasNeverStartedException();
        }
    }
}
