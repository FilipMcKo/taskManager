package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.internal.TaskDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TaskProgressService {

    private long taskDuration = TaskDuration.REGULAR.getDuration();
    @Autowired
    private TaskRepository taskRepository;

    public long getTaskDuration() {
        return taskDuration;
    }

    public void updateTaskProgress(Task task) {
        if(task.isNotRunning){
            return;
        }
        long currentDuration = Instant.now().toEpochMilli() - task.getTaskBeginTime();
        if (currentDuration >= taskDuration) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
            task.setNotRunning(true);
            taskRepository.update(task);
        } else {
            double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) taskDuration * 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            task.setProgressPercentage(currentPercentage);
            taskRepository.update(task);
        }
    }

    void updateTasksProgress() {
        taskRepository.getTaskList().forEach(this::updateTaskProgress);
    }
}
