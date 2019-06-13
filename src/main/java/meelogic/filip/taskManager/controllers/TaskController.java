package meelogic.filip.taskManager.controllers;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.TaskService;
import meelogic.filip.taskManager.services.TaskStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStateService taskStateService;
    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/tasksPageSorted")
    public Page<TaskDTO> getAllTasksPagedAndSorted(@RequestParam(defaultValue = "0") int pageNr,
                                                   @RequestParam(defaultValue = "id") String key,
                                                   @RequestParam(defaultValue = "true") String desc) {
        if (desc.equals("true")) {
            Page<Task> page = taskService.getAllTasksPagedAndSorted(PageRequest.of(pageNr, 5, Sort.by(key).descending()));
            return page.map(source -> mapperFacade.map(source, TaskDTO.class));
        }

        Page<Task> page = taskService.getAllTasksPagedAndSorted(PageRequest.of(pageNr, 5, Sort.by(key).ascending()));
        return page.map(source -> mapperFacade.map(source, TaskDTO.class));
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

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> addNewTask(@Valid @RequestBody TaskCreationRequest taskCreationRequest) {
        TaskDTO newTaskDTO = this.mapperFacade.map(taskService.addNewTask(taskCreationRequest), TaskDTO.class);
        return new ResponseEntity<>(newTaskDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Integer> removeTaskById(@PathVariable Integer id) {
        taskService.removeTaskById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTaskById(@PathVariable Integer id, @RequestBody String newName) {
        taskService.renameTaskById(id, newName);
    }

    @PutMapping("/tasks/{id}/start")
    public TaskDTO startProcessingTask(@PathVariable Integer id) {
        taskStateService.startProcessingTask(id);
        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }

    @PutMapping("/tasks/{id}/cancel")
    public TaskDTO cancelProcessingTask(@PathVariable Integer id) {
        taskStateService.cancelProcessingTask(id);
        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }
}
