package meelogic.filip.taskManager.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//docelowo ten moduł ma obsługiwać bazę danych więc dobrze byłoby go budować tak, żeby tylko on musiał być zmieniony gdy do tego dojdzie
@Repository
public class TaskRepository {

    @Autowired
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void removeById(Integer id) {
        this.taskList.removeIf(task -> task.getId().equals(id));
    }

    public void add(Task task) {
        this.taskList.add(task);
    }

    public void renameTask(String newName, Integer id) {
        for (Task task : this.taskList) {
            if(task.getId().equals(id)){
                task.setName(newName);
            }
        }
    }
}
