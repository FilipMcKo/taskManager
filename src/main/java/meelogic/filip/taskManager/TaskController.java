package meelogic.filip.taskManager;

import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id){
        taskMap.remove(id);
    }

    @PostMapping("/tasks")
    public Task newtask(@RequestBody Task newTask) {
        newTask.setId(Task.counter.incrementAndGet());
        taskMap.put(Task.counter.get(),newTask);
        return newTask;
    }

    @PutMapping("/tasks/{id}")
    public Task replaceTask(@RequestBody Task newTask, @PathVariable Integer id){
        if(taskMap.containsKey(id)){
            taskMap.replace(id,newTask);
        }
        else {
            newTask.setId(id);
            taskMap.put(id, newTask);
        }

        return newTask;
    }

}
