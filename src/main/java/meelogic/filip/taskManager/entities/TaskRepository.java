package meelogic.filip.taskManager.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//tutaj będzie miało miejsce łącznie się z bazą danych i parsowanie encji na listę, która będzie shareowana dalej
//tym sposobem jest to jedyna klasa wymagająca przebudowy przy wdrażaniu bazy danych
@Repository
public class TaskRepository {

    @Autowired
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return this.taskList;
    }
}
