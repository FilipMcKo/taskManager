package meelogic.filip.taskManager.controllers;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.controllers.responseStatusExceptions.EntityDoesNotExistException;
import meelogic.filip.taskManager.controllers.responseStatusExceptions.ForbiddenOperationException;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.TaskService;
import meelogic.filip.taskManager.services.TaskStateService;
import meelogic.filip.taskManager.services.exceptions.EntityDoesNotExistServiceException;
import meelogic.filip.taskManager.services.exceptions.ForbiddenOperationServiceException;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class TaskController {

    Logger logger = LoggerFactory.getLogger(SLF4JLogger.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStateService taskStateService;
    @Autowired
    private MapperFacade mapperFacade;

    /**
     * TODO:
     * Nie powinienem udostepniam klasy Task a TaskDTO
     * - pierwszy pomysł to streamy ale nie mogę do nowego obiektu Page<TaskDTO> tak po prostu przypisać elementów
     * - drugi pomysł to po prostu zwracanie listy tak jak poprzednio z tym, że z taskRepository dostaję Page, którą parsuję na List
     *   czyli zakres elementów by się zgadzał - NA RAZIE WYGLADA NA TO, ŻE DZIAŁA
     *
     *   uwaga: wprowadzam spowrotem wyświetlanie taksów a nie taskDTO bo cos mi we fronci enie wychodzi tak jak chcę i to może być to
     *   no i zadziałało jak zacząłem zwracać page zamiast list. Także muszę jednak zmapować jakoś obiekty w obrębie page  - ale to na później
     */

    @GetMapping("/tasksPage")
    public Page<Task> getAllTasksPaged(@RequestParam(defaultValue = "0") int page) {
        List<TaskDTO> taskDTOList = new LinkedList<>();
        taskService.getAllTasksPaged(PageRequest.of(page,4)).map(task -> taskDTOList.add(this.mapperFacade.map(task, TaskDTO.class)));
        return taskService.getAllTasksPaged(PageRequest.of(page,4));
    }

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
    public ResponseEntity<Integer> removeTaskById(@PathVariable Integer id) {
        try {
            taskService.removeTaskById(id);
            return new ResponseEntity<>(id,HttpStatus.OK);
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> addNewTask(@Valid @RequestBody TaskCreationRequest taskCreationRequest) {
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
    public TaskDTO startProcessingTask(@PathVariable Integer id) {
        try {
            taskStateService.startProcessingTask(id);
        } catch (ForbiddenOperationServiceException e) {
            throw new ForbiddenOperationException();
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }
        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }

    @PutMapping("/tasks/{id}/cancel")
    public TaskDTO cancelProcessingTask(@PathVariable Integer id) {
        try {
            taskStateService.cancelProcessingTask(id);
        } catch (ForbiddenOperationServiceException e) {
            throw new ForbiddenOperationException();
        } catch (EntityDoesNotExistServiceException e) {
            throw new EntityDoesNotExistException();
        }

        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }
}
