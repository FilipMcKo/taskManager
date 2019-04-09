package meelogic.filip.taskManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LoadDatabase {

    @Bean
    Map<Integer, Task> initDataBase(){
        Map<Integer, Task> taskMap = new HashMap<>();
        taskMap.put(1,new Task("Task1"));
        taskMap.put(2,new Task("Task2"));
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

