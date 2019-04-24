package meelogic.filip.taskManager.controllers;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.TaskService;
import meelogic.filip.taskManager.services.TaskStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStateService taskStateService;
    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> taskDTOList = new LinkedList<>();
        taskService.getAllTasks().forEach(task -> taskDTOList.add(this.mapperFacade.map(task, TaskDTO.class)));
        return taskDTOList;
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskById(@PathVariable Integer id) {
        return this.mapperFacade.map(taskService.getTaskById(id), TaskDTO.class);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Integer id) {
        taskService.removeTaskById(id);
    }

    @PostMapping("/tasks")
    public ResponseEntity<String> newTask(@Valid TaskCreationRequest taskCreationRequest) {
        taskService.addNewTask(taskCreationRequest);
        return new ResponseEntity<>("Entity created", HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTask(@PathVariable Integer id, @RequestBody String newName) {
        taskService.renameTaskById(id, newName);
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
