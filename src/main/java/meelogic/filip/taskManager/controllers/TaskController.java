package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.TaskDAO;
import meelogic.filip.taskManager.entities.TaskDTO;
import meelogic.filip.taskManager.services.Service;
import meelogic.filip.taskManager.services.TaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private Service service;
    @Autowired
    private TaskProcessor taskProcessor;

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        return service.getAllTasksDTOs();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskDTO(@PathVariable Integer id) {
        return service.getTaskDTObyId(id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        service.removeById(id);
    }

    @PostMapping("/tasks/newtask")
    public void newtask(@RequestBody TaskDAO taskDAO) {
        service.addNewTask(taskDAO);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTask(@RequestBody String newName, @PathVariable Integer id) {
        service.renameTask(newName, id);
    }

    @PutMapping("/tasks/{id}/start")
    public void startTask(@PathVariable Integer id) {
        taskProcessor.startProcessing(id);
    }

    @PutMapping("/tasks/{id}/cancel")
    public void cancelTask(@PathVariable Integer id) {
        taskProcessor.cancelProcessing(id);
    }

}
