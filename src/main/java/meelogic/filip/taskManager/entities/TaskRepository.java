package meelogic.filip.taskManager.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository {

    @Autowired
    static List<Task> taskList;

    public static List<Task> getTaskList(){
        return taskList;
    }

}
