package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.State;
import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.entities.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

//serwis, który będzie odpowiedzialny za nadawanie odpowiedniego stanu oraz za start taska (czyli po prostu nadania mu czasu startu na podstawie, którego będzie obliczany postęp
@Component
public class TaskProcessor {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private Service service;

    //public static final long durationInMillis = 20000;

    public void startProcessing(Integer id){
        Task task = service.getTaskById(id);
        task.setTaskBeginTime(System.currentTimeMillis());
        task.setCurrentState(State.RUNNING);
    }
}
