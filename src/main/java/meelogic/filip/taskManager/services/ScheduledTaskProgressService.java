package meelogic.filip.taskManager.services;

import com.sun.glass.ui.Application;
import meelogic.filip.taskManager.configurations.TestReceiver;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Deprecated
@Service
public class ScheduledTaskProgressService {

    @Autowired
    private TaskRepository taskRepository;

    public void updateTaskProgress(Task task) {
        if (task.getCurrentState() != State.RUNNING) {
            return;
        }
        long currentDuration = Instant.now().toEpochMilli() - task.getTaskBeginTime();
        if (currentDuration >= task.getCustomDuration()) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
        } else {
            double currentPercentage = BigDecimal.valueOf((double) currentDuration / (double) task.getCustomDuration() * 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            task.setProgressPercentage(currentPercentage);
        }
        taskRepository.save(task);
    }


    @Scheduled(fixedDelay = 1000)
    void updateTasksProgress() {
        taskRepository.findAll().forEach(this::updateTaskProgress);
    }

    @Scheduled(fixedDelay = 1000)
    void updateTaskPoolProgress(){
        //tutaj na każdym obiekcie w tasksPool muszę wywołać metodę updateTaskProgress
    }


}
