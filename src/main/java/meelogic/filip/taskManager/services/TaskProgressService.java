package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.internal.TaskDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaskProgressService {

    private long taskDuration = TaskDuration.REGULAR.getDuration();

    @Autowired
    private TaskRepository taskRepository;

    public long getTaskDuration() {
        return taskDuration;
    }

    public void updateTaskProgress(Task task) {
        // TODO: zło archeologia jest złą
        Long begin = task.getTaskBeginTime();
        if (begin == null) {
            return;
        }
        // TODO: instant
        long currentDuration = System.currentTimeMillis() - begin;
        if (currentDuration >= taskDuration) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
            taskRepository.update(task);
            // TODO: zło
            return;
        }
        double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) taskDuration * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        task.setProgressPercentage(currentPercentage);
        taskRepository.update(task);
    }

    void updateTasksProgress() {
        // TODO: streamy!
        for (Task task : taskRepository.getTaskList()) {
            this.updateTaskProgress(task);
        }
    }

}
