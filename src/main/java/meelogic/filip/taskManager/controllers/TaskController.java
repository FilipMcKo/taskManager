package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.external.TaskCreator;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.services.TaskCrudService;
import meelogic.filip.taskManager.services.TaskStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    // TODO: crud jest beee
    // TODO: scalic czy nie scalic?
    @Autowired
    private TaskCrudService taskCrudService;
    @Autowired
    private TaskStateService taskStateService;

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        // TODO: DTOs w nazwie metody
        return taskCrudService.getAllTaskDTOs();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskDTO(@PathVariable Integer id) {
        return taskCrudService.getTaskDTObyId(id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        taskCrudService.removeTaskById(id);
    }

    // TODO: zrypany endpoint
    @PostMapping("/tasks/newTask")
    public ResponseEntity<String> newTask(@RequestBody TaskCreator taskCreator) {
        taskCrudService.addNewTask(taskCreator);
        return new ResponseEntity<>("Entity created", HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTask(@PathVariable Integer id, @RequestBody String newName) {
        taskCrudService.renameTaskById(id, newName);
    }

    @PutMapping("/tasks/{id}/start")
    public void startTask(@PathVariable Integer id) {
        taskStateService.startProcessing(id);
    }

    @PutMapping("/tasks/{id}/cancel")
    public void cancelTask(@PathVariable Integer id) {
        taskStateService.cancelProcessing(id);
    }
}
