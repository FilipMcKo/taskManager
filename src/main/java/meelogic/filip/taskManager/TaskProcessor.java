package meelogic.filip.taskManager;

import java.util.Date;

public class TaskProcessor {

    void startProcessing(Task task){
        task.setTaskBeginTime(new Date().getTime());
        task.setCurrentState(State.RUNNING);
    }
}
