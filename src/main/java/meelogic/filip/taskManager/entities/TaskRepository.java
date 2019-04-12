package meelogic.filip.taskManager.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class should be linked with database in creation of next feature.
 * It should refresh a DB and taskList content every time getTaskList() is called
 */
@Repository
public class TaskRepository {

    @Autowired
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return this.taskList;
    }
}
