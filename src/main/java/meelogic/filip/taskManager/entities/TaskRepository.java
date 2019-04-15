package meelogic.filip.taskManager.entities;

import meelogic.filip.taskManager.entities.internal.Task;
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

    public void create(Task task) {
        this.taskList.add(task);
    }

    public void delete(Integer id) {
        this.taskList.removeIf(task -> task.getId().equals(id));
    }

    public Task read(Integer id) {
        return taskList.stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);
    }

    public void update(Task updatedTask) {
        Task task = this.read(updatedTask.getId());
        task = updatedTask;
    }
}
