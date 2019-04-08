package meelogic.filip.taskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private Map<Integer,Task> sampleTaskMap;

    @RequestMapping("/")
    public String index(){
        return "Welcome to Task Manager!";
    }

    @GetMapping("/tasks")
    public String getAllTasks(){
        return this.sampleTaskMap.toString();
    }

    @GetMapping("/tasks/{id}")
    public String getTask(@PathVariable Integer id){
        return this.sampleTaskMap.get(id).toString();
    }
}
