package meelogic.filip.taskManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class MyConfiguration {

    @Bean
    Map<Integer, Task> initDataBase() {
        Map<Integer, Task> taskMap = new HashMap<>();
        taskMap.put(1, new Task("Task1"));
        taskMap.put(2, new Task("Task2"));
        log.info("Sample tasks added");
        return taskMap;
    }

    @Bean
    TaskProcessor initTaskProcessor() {
        return new TaskProcessor();
    }
}

