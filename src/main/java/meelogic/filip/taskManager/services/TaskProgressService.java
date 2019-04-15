package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.internal.TaskDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaskProgressService {

    @Autowired
    private
    TaskRepository taskRepository;

    private void updateTaskProgress(Task task) {
        Long begin = task.getTaskBeginTime();
        if (begin == null) {
            return;
        }
        long currentDuration = System.currentTimeMillis() - begin;
        if (currentDuration >= TaskDuration.regular) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
            taskRepository.update(task);
            return;
        }
        double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) TaskDuration.regular * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        task.setProgressPercentage(currentPercentage);
        taskRepository.update(task);
    }

    void updateTasksProgress() {
        for (Task task : taskRepository.getTaskList()) {
            this.updateTaskProgress(task);
        }
    }
}
