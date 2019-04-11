package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.State;
import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.entities.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessor {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private Service service;

    public void startProcessing(Integer id){
        Task task = service.getTaskById(id);
        if(task.getCurrentState().equals(State.RUNNING)){
            return;
        }
        task.setTaskBeginTime(System.currentTimeMillis());
        task.setCurrentState(State.RUNNING);
    }

    public void cancelProcessing(Integer id){
        Task task = service.getTaskById(id);
        if(task.getCurrentState().equals(State.RUNNING)){
            task.setCurrentState(State.CANCELLED);
            task.setProgressPercentage(0.0);
            task.setTaskBeginTime(null);
        }
    }
}
