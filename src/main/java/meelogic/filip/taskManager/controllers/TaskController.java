package meelogic.filip.taskManager.controllers;

import io.micrometer.core.annotation.Timed;
import io.prometheus.client.Counter;
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

    private final Counter requestPage = Counter.build().name("requests_tasks_pages_total").help("Total requests of single task page.").register();
    private final Counter requestAddTask = Counter.build().name("requests_add_new_task_total").help("Total requests of creating new task.").register();
    private final Counter requestRemoveTask = Counter.build().name("requests_remove_task_total").help("Total requests of removing a task.").register();
    private final Counter requestStartTask = Counter.build().name("requests_start_task_total").help("Total requests of starting a task.").register();
    private final Counter requestCancelTask = Counter.build().name("requests_cancel_task_total").help("Total requests of canceling a task.").register();

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStateService taskStateService;
    @Autowired
    private MapperFacade mapperFacade;

    @Timed(value = "taskPageSorted",
            histogram = true,
            percentiles = {0.95, 0.99})
    @GetMapping("/tasksPageSorted")
    public Page<TaskDTO> getAllTasksPagedAndSorted(@RequestParam(defaultValue = "0") int pageNr,
                                                   @RequestParam(defaultValue = "id") String key,
                                                   @RequestParam(defaultValue = "true") String desc) {
        this.requestPage.inc();
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


    @Timed(value = "addNewTask",
            histogram = true,
            percentiles = {0.95, 0.99})
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> addNewTask(@Valid @RequestBody TaskCreationRequest taskCreationRequest) {
        TaskDTO newTaskDTO = this.mapperFacade.map(taskService.addNewTask(taskCreationRequest), TaskDTO.class);
        this.requestAddTask.inc();
        return new ResponseEntity<>(newTaskDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Integer> removeTaskById(@PathVariable Integer id) {
        taskService.removeTaskById(id);
        this.requestRemoveTask.inc();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}/rename")
    public void renameTaskById(@PathVariable Integer id, @RequestBody String newName) {
        taskService.renameTaskById(id, newName);
    }

    @Timed(value = "startTask",
            histogram = true,
            percentiles = {0.95, 0.99})
    @PutMapping("/tasks/{id}/start")
    public TaskDTO startProcessingTask(@PathVariable Integer id) {
        taskStateService.putTaskOnQueue(id);
        this.requestStartTask.inc();
        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }


    @Timed(value = "cancelTask",
            histogram = true,
            percentiles = {0.95, 0.99})
    @PutMapping("/tasks/{id}/cancel")
    public TaskDTO cancelProcessingTask(@PathVariable Integer id) {
        taskStateService.cancelProcessingTask(id);
        this.requestCancelTask.inc();
        return this.mapperFacade.map(taskService.getTaskById(id).get(), TaskDTO.class);
    }
}
