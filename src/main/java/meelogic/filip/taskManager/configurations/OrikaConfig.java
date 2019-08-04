package meelogic.filip.taskmanager.configurations;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import meelogic.filip.taskmanager.entities.external.TaskDTO;
import meelogic.filip.taskmanager.entities.internal.Task;
import meelogic.filip.taskmanager.entities.internal.TaskPriority;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfig {

    @Bean
    public MapperFacade getMapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory
            .classMap(Task.class, TaskDTO.class)
            .customize(new CustomMapper<Task, TaskDTO>() {
                @Override
                public void mapAtoB(Task task, TaskDTO taskDTO, MappingContext context) {
                    String priority = TaskPriority.getPriorityAsString(task.getPriority());
                    taskDTO.setPriority(priority);
                }
            })
            //.field("priority", TaskPriority.valueOf("priority").toString())
            .exclude("taskBeginTime")
            .exclude("customDuration")
            .byDefault()
            .register();
        return mapperFactory.getMapperFacade();
    }
}
