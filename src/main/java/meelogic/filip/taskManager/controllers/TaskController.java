package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.TaskDTO;
import meelogic.filip.taskManager.entities.TaskRepository;
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

    /**
     * Na razie zwracam kod 200 z pustym JSONem jeżeli brakuje takiego id w bazie
     * <p>
     * później mogę dodać info w JSONie o tym co się stało
     */
    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskDTO(@PathVariable Integer id) {
        return service.getTaskDTObyId(id);
    }


    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        service.removeById(id);
    }

    @PostMapping("/tasks/newtask")
    public void newtask(@RequestBody String taskName) {
        service.addNewTask(taskName);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTask(@RequestBody String newName, @PathVariable Integer id) {
        service.renameTask(newName, id);
    }

    @PutMapping("/tasks/{id}/start")
    public void startTask(@PathVariable Integer id) {
        taskProcessor.startProcessing(id);
    }

}
