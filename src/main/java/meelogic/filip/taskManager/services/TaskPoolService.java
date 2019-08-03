package meelogic.filip.taskmanager.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

import meelogic.filip.taskmanager.entities.internal.State;
import meelogic.filip.taskmanager.entities.internal.Task;
import meelogic.filip.taskmanager.services.exceptions.Preconditions;
import meelogic.filip.taskmanager.services.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static meelogic.filip.taskmanager.entities.internal.State.FINISHED;
import static meelogic.filip.taskmanager.entities.internal.State.RUNNING;
import static meelogic.filip.taskmanager.services.exceptions.OperationStatus.ENTITY_NOT_FOUND;

@Service
public class TaskPoolService {

    private List<Task> taskPool = new LinkedList<>();
    private static final int MAX_POOL_SIZE = 5;

    @Autowired
    private Channel channel;

    @Autowired
    private TaskRepository taskRepository;

    @Qualifier("getQueueName")
    @Autowired
    private String queue;

    public void updateTaskProgress(Task task) {
        long currentDuration = Instant.now().toEpochMilli() - task.getTaskBeginTime();
        if (currentDuration >= task.getCustomDuration()) {
            task.setCurrentState(State.FINISHED);
            task.setProgressPercentage(100.0);
        } else {
            double currentPercentage = BigDecimal
                .valueOf((double) currentDuration / (double) task.getCustomDuration() * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            task.setProgressPercentage(currentPercentage);
        }
        taskRepository.save(task);
    }


    @Scheduled(fixedDelay = 1000)
    void updateTaskPoolProgress() throws IOException {
        if (!taskPool.isEmpty()) {
            taskPool.forEach(this::updateTaskProgress);
            taskPool.removeIf(task -> task.getCurrentState() == FINISHED);
        }
        if (taskPool.size() < MAX_POOL_SIZE) {
            GetResponse response = channel.basicGet(queue, true);
            if (response != null) {
                Integer taskId = byteArrToInt(response.getBody());
                Optional<Task> optTask = taskRepository.findById(taskId);
                Preconditions.checkArgument(optTask.isPresent(), ENTITY_NOT_FOUND);
                Task task = optTask.get();
                task.setTaskBeginTime(Instant.now().toEpochMilli());
                task.setCurrentState(RUNNING);
                taskRepository.save(task);
                taskPool.add(task);
            }
        }
    }

    private int byteArrToInt(byte[] msg) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : msg) {
            stringBuilder.append((char) b);
        }
        return Integer.parseInt(stringBuilder.toString());
    }
}
