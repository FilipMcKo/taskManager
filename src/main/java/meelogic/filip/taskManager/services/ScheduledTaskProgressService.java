package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.internal.TaskDuration;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class ScheduledTaskProgressService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskQueueService taskQueueService;

    private long taskDuration = TaskDuration.REGULAR.getDuration();

    public long getTaskDuration() {
        return taskDuration;
    }

    public void updateTaskProgress(Task task) {
        if (task.getCurrentState() != State.RUNNING) {
            return;
        }
        long currentDuration = Instant.now().toEpochMilli() - task.getTaskBeginTime();
        if (currentDuration >= taskDuration) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
            //taskQueueService.publishToQueue(task);
        } else {
            double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) taskDuration * 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            task.setProgressPercentage(currentPercentage);
        }
        taskRepository.save(task);
    }

    @Scheduled(fixedDelay = 1000)
    void updateTasksProgress() {
        taskRepository.findAll().forEach(this::updateTaskProgress);
    }
}
