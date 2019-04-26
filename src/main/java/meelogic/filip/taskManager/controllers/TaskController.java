package meelogic.filip.taskManager.controllers;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.controllers.responseStatusExceptions.EntityDoesNotExistException;
import meelogic.filip.taskManager.controllers.responseStatusExceptions.ForbiddenOperationException;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.TaskService;
import meelogic.filip.taskManager.services.TaskStateService;
import meelogic.filip.taskManager.services.exceptions.EntityDoesNotExistServiceException;
import meelogic.filip.taskManager.services.exceptions.ForbiddenOperationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        Optional<Task> optTask = taskService.getTaskById(id);
        if (!optTask.isPresent()) {
            throw new EntityDoesNotExistException();
        }
        return this.mapperFacade.map(optTask.get(), TaskDTO.class);
    }

    @DeleteMapping("/tasks/{id}")
    public void removeTaskById(@PathVariable Integer id) {
        try {
            taskService.removeTaskById(id);
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> addNewTask(@Valid TaskCreationRequest taskCreationRequest) {
        TaskDTO newTaskDTO = this.mapperFacade.map(taskService.addNewTask(taskCreationRequest), TaskDTO.class);
        return new ResponseEntity<>(newTaskDTO, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTaskById(@PathVariable Integer id, @RequestBody String newName) {
        try {
            taskService.renameTaskById(id, newName);
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
    }

    @PutMapping("/tasks/{id}/start")
    public void startProcessingTask(@PathVariable Integer id) {
        try {
            taskStateService.startProcessingTask(id);
        } catch (ForbiddenOperationServiceException e) {
            throw new ForbiddenOperationException();
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
    }

    @PutMapping("/tasks/{id}/cancel")
    public void cancelProcessingTask(@PathVariable Integer id) {
        try {
            taskStateService.cancelProcessingTask(id);
        } catch (ForbiddenOperationServiceException e) {
            throw new ForbiddenOperationException();
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
    }
}
