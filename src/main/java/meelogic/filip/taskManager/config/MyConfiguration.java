package meelogic.filip.taskManager.config;

import lombok.extern.slf4j.Slf4j;
import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.services.TaskProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class MyConfiguration {

    @Bean
    public Map<Integer, Task> initDataBase() {
        Map<Integer, Task> taskMap = new HashMap<>();
        taskMap.put(1, new Task("Task1"));
        taskMap.put(2, new Task("Task2"));
        log.info("Sample tasks added");
        return taskMap;
    }

    @Bean
    public TaskProcessor initTaskProcessor() {
        return new TaskProcessor();
    }
}

