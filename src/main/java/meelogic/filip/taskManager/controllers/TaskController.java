package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.external.TaskCreator;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.services.CrudService;
import meelogic.filip.taskManager.services.TaskProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private CrudService crudService;
    @Autowired
    private TaskProcessorService taskProcessorService;

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        return crudService.getAllTasksDTOs();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskDTO(@PathVariable Integer id) {
        return crudService.getTaskDTObyId(id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        crudService.removeById(id);
    }

    @PostMapping("/tasks/newtask")
    public void newtask(@RequestBody TaskCreator taskCreator) {
        crudService.addNewTask(taskCreator);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTask(@RequestBody String newName, @PathVariable Integer id) {
        crudService.renameTask(newName, id);
    }

    @PutMapping("/tasks/{id}/start")
    public void startTask(@PathVariable Integer id) {
        taskProcessorService.startProcessing(id);
    }

    @PutMapping("/tasks/{id}/cancel")
    public void cancelTask(@PathVariable Integer id) {
        taskProcessorService.cancelProcessing(id);
    }

}
