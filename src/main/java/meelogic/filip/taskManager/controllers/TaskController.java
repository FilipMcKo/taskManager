package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.TaskDTO;
import meelogic.filip.taskManager.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    Service service;

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        return service.getAllTasks();
    }






    /*@GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Integer id) {
        return taskMap.get(id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        taskMap.remove(id);
    }

    @PostMapping("/tasks")
    public Task newtask(@RequestBody Task newTask) {
        newTask.setId(Task.counter.incrementAndGet());
        taskProcessor.startProcessing(newTask);
        taskMap.put(Task.counter.get(), newTask);
        return newTask;
    }

    @PutMapping("/tasks/{id}")
    public Task replaceTask(@RequestBody Task newTask, @PathVariable Integer id) {
        if (taskMap.containsKey(id)) {
            taskMap.replace(id, newTask);
        } else {
            newTask.setId(id);
            taskMap.put(id, newTask);
        }

        return newTask;
    }*/

}
