package meelogic.filip.taskManager.services;

import com.rabbitmq.client.AMQP;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class TaskPoolService {
    private List<Task> taskPool = new LinkedList<>();
    private final int maxPoolSize = 5;

    @Autowired
    private AMQP.Channel channel;

    @Autowired
    private TaskRepository taskRepository;


    public void updateTaskProgress(Task task) {
        //ta metoda powinna przeliczac stan taskow w taskPool i zapisywac wynik w prawdziwej bazie
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
            if(task.getCurrentState()==State.FINISHED){
                taskPool.remove(task);
            }
        }

        if(taskPool.size()<maxPoolSize){

        }
    }

    @Scheduled(fixedDelay = 1000)
    void updateTaskPoolProgress(){
        taskPool.forEach(this::updateTaskProgress);
        updateTaskPool();
    }
}
