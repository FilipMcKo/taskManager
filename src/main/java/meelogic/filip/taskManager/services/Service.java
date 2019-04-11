package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Obowiązki tego serwisu:
 * 1. pozyskiwanie z task repository listy obiektów
 * 2. wykonywanie na tej liście CRUDowych operacji
 * 3. nadawanie id'ków taskom
 * 4. aktualizowanie procentowego stanu zaawansowania tasków
 *
 * Cel jest taki aby klasa TaskRepository udostępniała dane w postaci listy tak żebym tu nie musiał
 * już nic zmieniać, bo ta klasa ma po prostu przyjmować listę
 */


@Component
public class Service {

    //początkowa wartość jest równa 3 przez to, że w sampleDAtaBaseConfig dodaję 2 obiekty
    //docelowo ta wartość ma być równa 0 i będzie się zawsze ładnie inkrementować przy dodawania nowego taska
    // i będę miał pewność, że nie wystąpią powtórzenia indeksu
    private static final AtomicInteger counter = new AtomicInteger(3);

    @Autowired
    private TaskRepository taskRepository;

    private List<Task> taskList = taskRepository.getTaskList();

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
        double currentPercentage = BigDecimal.valueOf((double)currentDuration /(double) TaskDuration.regular * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        task.setProgressPercentage(currentPercentage);
    }

    private void updateTasks() {
        for (Task task : taskRepository.getTaskList()) {
            this.updateTask(task);
        }
    }

    public List<TaskDTO> getAllTasksDTOs() {
        this.updateTasks();
        List<TaskDTO> taskDTOList = new LinkedList<>();
        for (Task task : this.taskList) {
            taskDTOList.add(TaskDTOBuilder.taskToTaskDTO(task));
        }
        return taskDTOList;
    }

    public TaskDTO getTaskDTObyId(Integer id) {
        this.updateTasks();
        Optional<Task> taskOptional = this.taskList.stream().filter(t -> t.getId().equals(id)).findAny();
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
        this.taskList.removeIf(task -> task.getId().equals(id));
    }

    public void addNewTask(TaskDAO taskDAO) {
        Integer id = counter.getAndIncrement();
        this.taskList.add(new Task(id, taskDAO.getName(), taskDAO.getDecription(), State.NONE, 0.0, null));
    }

    public void renameTask(String newName, Integer id) {
        for (Task task : this.taskList) {
            if(task.getId().equals(id)){
                task.setName(newName);
            }
        }
    }
}
