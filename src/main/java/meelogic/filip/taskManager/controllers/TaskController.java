package meelogic.filip.taskManager.controllers;

import ma.glasnost.orika.MapperFacade;
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

import javax.persistence.EntityNotFoundException;
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
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Integer id) {
        Optional<Task> optTask = taskService.getTaskById(id);
        return optTask.isPresent() ?
                new ResponseEntity<>(this.mapperFacade.map(optTask.get(), TaskDTO.class), HttpStatus.OK) :
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //TODO: responseEntity
    @DeleteMapping("/tasks/{id}")
    public void removeTaskById(@PathVariable Integer id) {
        taskService.removeTaskById(id);
    }

    //TODO: responseEntity
    @PostMapping("/tasks")
    public ResponseEntity<String> addNewTask(@Valid TaskCreationRequest taskCreationRequest) {
        TaskDTO newTaskDTO = this.mapperFacade.map(taskService.addNewTask(taskCreationRequest), TaskDTO.class);
        return new ResponseEntity<>(newTaskDTO.getId().toString(), HttpStatus.CREATED);
    }

    //TODO: responseEntity
    @PutMapping("/tasks/{id}/rename")
    public void renameTaskById(@PathVariable Integer id, @RequestBody String newName) {
        taskService.renameTaskById(id, newName);
    }

    //TODO: responseEntity
    @PutMapping("/tasks/{id}/start")
    public void startProcessingTask(@PathVariable Integer id) {
        taskStateService.startProcessingTask(id);
    }

    //TODO: responseEntity
    @PutMapping("/tasks/{id}/cancel")
    public void cancelProcessingTask(@PathVariable Integer id) {
        try{
            taskStateService.cancelProcessingTask(id);
        } catch(EntityDoesNotExistServiceException e){
            throw new EntityNotFoundException();
        } catch(ForbiddenOperationServiceException e){
            throw new ForbiddenOperationException();
        }
    }
}
