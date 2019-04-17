package meelogic.filip.taskManager.services;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import meelogic.filip.taskManager.entities.external.TaskCreator;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.LinkedList;
import java.util.List;

@Service
public class TaskCrudService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskProgressService taskProgressService;
    private MapperFacade mapperFacade;

    TaskCrudService() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Task.class, TaskDTO.class).exclude("taskBeginTime").byDefault().register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public List<TaskDTO> getAllTaskDTOs() {
        taskProgressService.updateTasksProgress();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        for (Task task : this.taskRepository.getTaskList()) {
            taskDTOList.add(this.mapperFacade.map(task, TaskDTO.class));
        }
        return taskDTOList;
    }

    public TaskDTO getTaskDTObyId(Integer id) {
        taskProgressService.updateTasksProgress();
        Task task = this.taskRepository.read(id);
        return this.mapperFacade.map(task, TaskDTO.class);
    }

    public void removeTaskById(Integer id) {
        this.taskRepository.delete(id);
    }

    public void addNewTask(TaskCreator taskCreator) {
        Task task = new Task();
        task.setName(taskCreator.getName());
        task.setDescription(taskCreator.getDecription());
        task.setCurrentState(State.NONE);
        task.setProgressPercentage(0.0);
        this.taskRepository.create(task);
    }

    public void renameTaskById(Integer id, String newName) {
        Task task = this.taskRepository.read(id);
        task.setName(newName);
        this.taskRepository.update(task);
    }
}
