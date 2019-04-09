package meelogic.filip.taskManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class MyConfig {

    @Bean
    Map<Integer, Task> initDataBase(){
        Map<Integer, Task> taskMap = new HashMap<>();
        taskMap.put(1,new Task(0,"Task1",State.NONE,0.0));
        taskMap.put(2,new Task(0,"Task2",State.NONE,0.0));
        return taskMap;
    }

  /*  @Bean
    public Map<Integer,Task> getSampleTaskMap(){
        return new TaskParser().getSampleTaskMap();
    }*/

}


/**
 * TODO: przy przenoszeniu tego na bazę danych:
 * 1. Zmienić taks na Entity
 * 2. Dodać odpowiednie adnotacje w Entity
 * 3. Utworzyć OrderRepository które implementuje JPARepository
 * 4.
 */

