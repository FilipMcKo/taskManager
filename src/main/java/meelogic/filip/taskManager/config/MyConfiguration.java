package meelogic.filip.taskManager.config;

import lombok.extern.slf4j.Slf4j;
import meelogic.filip.taskManager.entities.State;
import meelogic.filip.taskManager.entities.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedList;
import java.util.List;

@Configuration
@Slf4j
public class MyConfiguration {

    @Bean
    public List<Task> initDataBase() {
        List<Task> taskList = new LinkedList<>();
        taskList.add(new Task(1,"Task1", State.NONE, 0.0,null));
        taskList.add(new Task(2,"Task2", State.NONE, 0.0,null));
        log.info("Sample tasks added");
        return taskList;
    }
}

