package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Service {

    //początkowa wartość jest równa 3 przez to, że w sampleDAtaBaseConfig dodaję 2 obiekty
    //docelowo ta wartość ma być równa 0 i będzie się zawsze ładnie inkrementować przy dodawania nowego taska
    // i będę miał pewność, że nie wystąpią powtórzenia indeksu
    private static final AtomicInteger counter = new AtomicInteger(3);

    @Autowired
    private TaskRepository taskRepository;

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
        task.setProgressPercentage((double) ((currentDuration / TaskDuration.regular) * 100));
    }

    private void updateTasks() {
        for (Task task : taskRepository.getTaskList()) {
            this.updateTask(task);
        }
    }

    //w tej konfiguracji mam sytuację w której w każdej metodzie taskList jest inicjowany na nowo za pomocą taskRepository
    //czy to jest dobrze czy źle? być może dobrze
    // trochę nie wiem jak to zrobić inaczej bo jak inicjowałem to w konstruktorze to się burzył - pewnie dlatego że to jest lazy
    // i jest realizowane DI dopiero jak trzeba, a w innym przypadku ta referencja jest nullem
    private List<Task> taskList;

    public List<TaskDTO> getAllTasksDTOs() {
        this.updateTasks();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        this.taskList = taskRepository.getTaskList();
        for (Task task : taskList) {
            taskDTOList.add(TaskDTOBuilder.taskToTaskDTO(task));
        }
        return taskDTOList;
    }

    public TaskDTO getTaskDTObyId(Integer id) {
        this.updateTasks();
        this.taskList = taskRepository.getTaskList();
        Optional<Task> taskOptional = taskList.stream().filter(t -> t.getId().equals(id)).findAny();
        if (taskOptional.isPresent()) {
            return TaskDTOBuilder.taskToTaskDTO(taskOptional.get());
        }
        return null;
    }

    public Task getTaskById(Integer id) {
        this.updateTasks();
        return taskList.stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);
    }

    public void removeById(Integer id) {
        taskRepository.removeById(id);
    }

    public void addNewTask(String taskName) {
        Integer id = counter.getAndIncrement();
        taskRepository.add(new Task(id, taskName, State.NONE, 0.0, null));
    }

    public void renameTask(String newName, Integer id) {
        taskRepository.renameTask(newName, id);
    }
}
