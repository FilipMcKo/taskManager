package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskStateService {
    @Autowired
    private TaskRepository taskRepository;

    public void startProcessing(Integer id) {
        Task task = taskRepository.read(id);
        if (task.getCurrentState().equals(State.RUNNING)) {
            return;
        }
        task.setTaskBeginTime(System.currentTimeMillis());
        task.setCurrentState(State.RUNNING);
        taskRepository.update(task);
    }

    public void cancelProcessing(Integer id) {
        Task task = taskRepository.read(id);
        if (task.getCurrentState().equals(State.RUNNING)) {
            task.setCurrentState(State.CANCELLED);
            task.setProgressPercentage(0.0);
            task.setTaskBeginTime(null);
            taskRepository.update(task);
        }
    }
}
