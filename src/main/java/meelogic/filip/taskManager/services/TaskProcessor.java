package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.State;
import meelogic.filip.taskManager.entities.Task;

import java.util.Date;

public class TaskProcessor {

    public void startProcessing(Task task){
        task.setTaskBeginTime(new Date().getTime());
        task.setCurrentState(State.RUNNING);
    }
}
