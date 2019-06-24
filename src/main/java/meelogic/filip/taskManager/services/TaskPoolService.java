package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static meelogic.filip.taskManager.entities.internal.State.RUNNING;
import static meelogic.filip.taskManager.services.exceptions.OperationStatus.ENTITY_NOT_FOUND;

@Service
public class TaskPoolService {
    private List<Task> taskPool = new LinkedList<>();
    private final int maxPoolSize = 5;

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

    @Scheduled(fixedDelay = 1000)
    void updateTaskPoolProgress() {
        if (!taskPool.isEmpty()) {
            taskPool.forEach(this::updateTaskProgress);
            updateTaskPool();
        }
    }

    @RabbitListener(queues = "myQueue")
    public void receive(String in) {
        if (taskPool.size() < maxPoolSize) {
            Optional<Task> optTask = taskRepository.findById(Integer.valueOf(in));
            Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
            Task task = optTask.get();
            task.setTaskBeginTime(Instant.now().toEpochMilli());
            task.setCurrentState(RUNNING);
            taskRepository.save(task);
            taskPool.add(task);
        }
    }
}
