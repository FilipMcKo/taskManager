package meelogic.filip.taskManager.services;

import com.rabbitmq.client.Channel;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class TaskPoolService {
    private List<Task> taskPool = new LinkedList<>();
    private final int maxPoolSize = 5;
    private static final String QUEUE_NAME = "myQueue";
    private static final String FANOUT_EXCHANGE = "myExchange";

    @Autowired
    private Channel channel;

    @Autowired
    private TaskRepository taskRepository;

    public void updateTaskProgress(Task task) {
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

    private void updateTaskPool() {
        for (Task task : taskPool) {
            if (task.getCurrentState() == State.FINISHED) {
                taskPool.remove(task);
            }
        }
    }

    @Scheduled(fixedDelay = 300)
    void updateTaskPoolProgress() throws IOException {
//        if (!taskPool.isEmpty()) {
//            taskPool.forEach(this::updateTaskProgress);
//            updateTaskPool();
//        }
        System.out.println("basicGet");
        channel.basicGet(QUEUE_NAME, true);
    }

}
