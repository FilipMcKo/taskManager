package meelogic.filip.taskManager;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class TaskController {
    
    private final Map<Integer, Task> taskMap;
    private TaskProcessor taskProcessor;

    public TaskController(Map<Integer, Task> taskMap, TaskProcessor taskProcessor) {
        this.taskMap = taskMap;
        this.taskProcessor = taskProcessor;
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
        taskProcessor.startProcessing(newTask);
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
