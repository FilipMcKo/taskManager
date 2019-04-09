package meelogic.filip.taskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TaskController {
    
    private final Map<Integer, Task> taskMap;

    public TaskController(Map<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }


    @RequestMapping("/")
    public String index(){
        return "Welcome to Task Manager!";
    }

    @GetMapping("/tasks")
    public Map<Integer,Task> getAllTasks(){
        return taskMap;
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Integer id){
        return taskMap.get(id);
    }
}
