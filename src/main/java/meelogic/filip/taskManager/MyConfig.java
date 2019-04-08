package meelogic.filip.taskManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MyConfig {

    @Bean
    public Map<Integer,Task> getSampleTaskMap(){
        return new TaskParser().getSampleTaskMap();
    }

}
