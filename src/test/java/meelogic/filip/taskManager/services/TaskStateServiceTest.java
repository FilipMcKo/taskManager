package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class TaskStateServiceTest {

    @TestConfiguration
    static class TaskStateServiceTestContextConfiguration{
        @Bean
        public TaskStateService taskStateService(){
            return new TaskStateService();
        }
    }

    @Autowired
    public TaskStateService taskStateService;

    @MockBean
    public TaskRepository taskRepository;

   /* @Before
    public void setUp(){
        Task sampleTask = new Task(1,"Task2","Sample task nr two", State.NONE, 0.0,null);
        Mockito.when(taskRepository.read(sampleTask.getId())).thenReturn(sampleTask);
    }*/

    @Test
    void startProcessing() {
        Task sampleTask = new Task(1,"Task2","Sample task nr two", State.NONE, 0.0,null);
        Mockito.when(taskRepository.read(sampleTask.getId())).thenReturn(sampleTask);
        //this.taskStateService.startProcessing(1);
        //assertEquals(State.RUNNING,sampleTask.getCurrentState());
    }
}