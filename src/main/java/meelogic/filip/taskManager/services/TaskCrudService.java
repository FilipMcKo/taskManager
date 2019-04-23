package meelogic.filip.taskManager.services;

import ma.glasnost.orika.MapperFacade;
import meelogic.filip.taskManager.services.exceptions.EntityDoesNotExistException;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;

@Service
public class TaskCrudService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskProgressService taskProgressService;
    @Autowired
    private MapperFacade mapperFacade;

    public List<TaskDTO> getAllTasks() {
        taskProgressService.updateTasksProgress();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        this.taskRepository.getTaskList().forEach(task -> taskDTOList.add(this.mapperFacade.map(task, TaskDTO.class)));
        return taskDTOList;
    }

    public TaskDTO getTaskDTObyId(Integer id) {
        taskProgressService.updateTasksProgress();
        Task task;
        try {
            task = this.taskRepository.read(id);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
        // TODO: controller?
        return this.mapperFacade.map(task, TaskDTO.class);
    }

    public void removeTaskById(Integer id) {
        try {
            this.taskRepository.delete(id);
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
        this.taskRepository.create(task);
    }

    public void renameTaskById(Integer id, String newName) {
        Task task;
        try {
            task = this.taskRepository.read(id);
            task.setName(newName);
            this.taskRepository.update(task);
        } catch (EntityNotFoundException e) {
            throw new EntityDoesNotExistException();
        }
    }
}
