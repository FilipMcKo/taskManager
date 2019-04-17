package meelogic.filip.taskManager.configurations;

import lombok.extern.slf4j.Slf4j;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedList;
import java.util.List;

@Configuration
@Slf4j
public class SampleDataBaseConfig {

    @Bean
    public List<Task> initInMemoryDataBase() {
        List<Task> taskList = new LinkedList<>();
        Task task1 = new Task(1,"Task1", "Sample task nr one", State.NONE, 0.0,null);
        Task task2 = new Task(2,"Task2","Sample task nr one", State.NONE, 0.0,null);
        taskList.add(task1);
        taskList.add(task2);
        log.info("Sample tasks added to in-memory data base");
        return taskList;
    }
}

