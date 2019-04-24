package meelogic.filip.taskManager.services;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.services.exceptions.EntityDoesNotExistException;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskProgressService taskProgressService;
    @Autowired
    private MapperFacade mapperFacade;

    public List<TaskDTO> getAllTasks() {
        //taskProgressService.updateTasksProgress();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        this.taskRepository.findAll().forEach(task -> taskDTOList.add(this.mapperFacade.map(task, TaskDTO.class)));
        return taskDTOList;
    }

    public TaskDTO getTaskById(Integer id) {
        //taskProgressService.updateTasksProgress();
        Optional<Task> task;
        try {
            task = this.taskRepository.findById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
        return this.mapperFacade.map(task.get(), TaskDTO.class);
    }

    public void removeTaskById(Integer id) {
        try {
            this.taskRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
    }

    public void addNewTask(TaskCreationRequest taskCreationRequest) {
        Task task = new Task();
        task.setName(taskCreationRequest.getName());
        task.setDescription(taskCreationRequest.getDecription());
        task.setCurrentState(State.NEW);
        task.setProgressPercentage(0.0);
        task.setNotRunning(true);
        this.taskRepository.save(task);
    }

    public void renameTaskById(Integer id, String newName) {
        Optional<Task> task;
        try {
            task = this.taskRepository.findById(id);
            task.get().setName(newName);
            this.taskRepository.save(task.get());
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
    }
}
