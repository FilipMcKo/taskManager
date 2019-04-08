package meelogic.filip.taskManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Slf4j
public class MyConfig {

    @Bean
    void initDataBase(TaskRepository taskRepository){
        taskRepository.save(new Task(0,"Task1",State.NONE,0.0));
        taskRepository.save(new Task(0,"Task2",State.NONE,0.0));
    }

    @Bean
    public Map<Integer,Task> getSampleTaskMap(){
        return new TaskParser().getSampleTaskMap();
    }

}
