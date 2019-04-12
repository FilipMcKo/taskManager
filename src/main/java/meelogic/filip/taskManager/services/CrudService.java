package meelogic.filip.taskManager.services;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import meelogic.filip.taskManager.entities.*;
import meelogic.filip.taskManager.entities.external.TaskCreator;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.internal.TaskDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Duties of this service:
 * 1. getting a taskList from taskRepository bean
 * 2. making CRUD operations within this taskList
 * 3. giving proper id to task objects
 * 4. updating percentage progress every time it is asked of something
 */

@Component
public class CrudService {

    private static final AtomicInteger counter = new AtomicInteger(3);
    @Autowired
    private TaskRepository taskRepository;
    private List<Task> taskList;
    private MapperFacade mapperFacade;

    CrudService(){
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Task.class, TaskDTO.class).exclude("taskBeginTime").byDefault().register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    private void updateTask(Task task) {
        Long begin = task.getTaskBeginTime();
        if (begin == null) {
            return;
        }
        long currentDuration = System.currentTimeMillis() - begin;
        if (currentDuration >= TaskDuration.regular) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
            return;
        }
        double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) TaskDuration.regular * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        task.setProgressPercentage(currentPercentage);
    }

    private void updateTasks() {
        this.taskList = taskRepository.getTaskList();
        for (Task task : taskRepository.getTaskList()) {
            this.updateTask(task);
        }
    }

    public List<TaskDTO> getAllTasksDTOs() {
        this.taskList = taskRepository.getTaskList();
        this.updateTasks();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        for (Task task : this.taskList) {
            taskDTOList.add(TaskDTOBuilder.taskToTaskDTO(task));
        }
        return taskDTOList;
    }

    public TaskDTO getTaskDTObyId(Integer id) {
        this.taskList = taskRepository.getTaskList();
        this.updateTasks();
        Optional<Task> taskOptional = this.taskList.stream().filter(t -> t.getId().equals(id)).findAny();
        return taskOptional.map(task -> this.mapperFacade.map(task, TaskDTO.class)).orElse(null);
    }

    public Task getTaskById(Integer id) {
        this.taskList = taskRepository.getTaskList();
        this.updateTasks();
        return taskList.stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);
    }

    public void removeById(Integer id) {
        this.taskList = taskRepository.getTaskList();
        this.taskList.removeIf(task -> task.getId().equals(id));
    }

    public void addNewTask(TaskCreator taskCreator) {
        this.taskList = taskRepository.getTaskList();
        Integer id = counter.getAndIncrement();
        this.taskList.add(new Task(id, taskCreator.getName(), taskCreator.getDecription(), State.NONE, 0.0, null));
    }

    public void renameTask(String newName, Integer id) {
        this.taskList = taskRepository.getTaskList();
        for (Task task : this.taskList) {
            if (task.getId().equals(id)) {
                task.setName(newName);
            }
        }
    }
}
