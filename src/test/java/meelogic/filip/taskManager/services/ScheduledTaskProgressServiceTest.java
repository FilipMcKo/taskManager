package meelogic.filip.taskmanager.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import meelogic.filip.taskmanager.entities.internal.State;
import meelogic.filip.taskmanager.entities.internal.Task;
import meelogic.filip.taskmanager.entities.internal.TaskPriority;
import meelogic.filip.taskmanager.services.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduledTaskProgressServiceTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    private Task task6;
    private Task task7;
    private Task task8;
    List<Task> taskList;
    @Mock
    private TaskRepository taskRepositoryMock;
    @InjectMocks
    private TaskPoolService taskPoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        task1 = new Task(1, "Task1", "Sample task nr one", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task2 = new Task(2, "Task2", "Sample task nr two", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task3 = new Task(3, "Task3", "Sample task nr three", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task4 = new Task(4, "Task4", "Sample task nr four", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task5 = new Task(5, "Task5", "Sample task nr five", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task6 = new Task(6, "Task6", "Sample task nr six", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task7 = new Task(7, "Task7", "Sample task nr seven", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        task8 = new Task(8, "Task8", "Sample task nr eight", State.RUNNING, 0.0, null, 100000l, TaskPriority.HIGH);
        taskList = new LinkedList<>(Arrays.asList(task1, task2, task3, task4, task5, task6, task7, task8));
        Mockito.when(taskRepositoryMock.findAll()).thenReturn(this.taskList);
        ReflectionTestUtils.setField(taskPoolService, "taskPool", taskList);
        task1.setTaskBeginTime(Instant.now().toEpochMilli() - task1.getCustomDuration() / 2);
        task2.setTaskBeginTime(Instant.now().toEpochMilli() - task2.getCustomDuration() / 4);
        task3.setTaskBeginTime(Instant.now().toEpochMilli());
        task4.setTaskBeginTime(Instant.now().toEpochMilli() - 2 * task4.getCustomDuration());
        task5.setTaskBeginTime(Instant.now().toEpochMilli());
        task6.setTaskBeginTime(Instant.now().toEpochMilli());
        task7.setTaskBeginTime(Instant.now().toEpochMilli());
        task8.setTaskBeginTime(Instant.now().toEpochMilli());
    }

    @Test
    void shouldUpdateSingleTaskProgress() {
        taskPoolService.updateTaskProgress(task1);
        double progressPercentage1 = task1.getProgressPercentage();

        taskPoolService.updateTaskProgress(task2);
        double progressPercentage2 = task2.getProgressPercentage();

        taskPoolService.updateTaskProgress(task3);
        double progressPercentage3 = task3.getProgressPercentage();

        taskPoolService.updateTaskProgress(task4);
        double progressPercentage4 = task4.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
            () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
            () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0),
            () -> assertEquals(100.0, progressPercentage4));
    }

    @Test
    void shouldUpdateAllTasksProgress() throws IOException {

        taskPoolService.updateTaskPoolProgress();
        double progressPercentage1 = task1.getProgressPercentage();
        double progressPercentage2 = task2.getProgressPercentage();
        double progressPercentage3 = task3.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
            () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
            () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0));
    }
}