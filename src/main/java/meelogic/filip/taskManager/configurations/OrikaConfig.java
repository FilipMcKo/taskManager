package meelogic.filip.taskManager.configurations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfig {
    @Bean
    public MapperFacade getMapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Task.class, TaskDTO.class).exclude("taskBeginTime").byDefault().register();
        return mapperFactory.getMapperFacade();
    }
}
