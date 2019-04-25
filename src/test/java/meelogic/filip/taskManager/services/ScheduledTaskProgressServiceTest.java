package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledTaskProgressServiceTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    List<Task> taskList;
    @Mock
    private TaskRepository taskRepositoryMock;
    @InjectMocks
    private ScheduledTaskProgressService scheduledTaskProgressService;

    @BeforeEach
    void setUp() {
        task1 = new Task(1, "Task1", "Sample task nr one", State.NEW, 0.0, null,false);
        task2 = new Task(2, "Task2", "Sample task nr two", State.NEW, 0.0, null,false);
        task3 = new Task(3, "Task3", "Sample task nr three", State.NEW, 0.0, null,false);
        task4 = new Task(4, "Task4", "Sample task nr four", State.NEW, 0.0, null,false);
        task5 = new Task(4, "Task5", "Sample task nr five", State.NEW, 0.0, null,false);
        taskList = new LinkedList<>(Arrays.asList(task1, task2, task3));
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskRepositoryMock.findAll()).thenReturn(this.taskList);
        task1.setTaskBeginTime(Instant.now().toEpochMilli() - scheduledTaskProgressService.getTaskDuration() / 2);
        task2.setTaskBeginTime(Instant.now().toEpochMilli() - scheduledTaskProgressService.getTaskDuration() / 4);
        task3.setTaskBeginTime(Instant.now().toEpochMilli());
        task4.setTaskBeginTime(Instant.now().toEpochMilli() - 2 * scheduledTaskProgressService.getTaskDuration());
    }

    @Test
    void shouldUpdateSingleTaskProgress() {
        scheduledTaskProgressService.updateTaskProgress(task1);
        double progressPercentage1 = task1.getProgressPercentage();

        scheduledTaskProgressService.updateTaskProgress(task2);
        double progressPercentage2 = task2.getProgressPercentage();

        scheduledTaskProgressService.updateTaskProgress(task3);
        double progressPercentage3 = task3.getProgressPercentage();

        scheduledTaskProgressService.updateTaskProgress(task4);
        double progressPercentage4 = task4.getProgressPercentage();

        double progressPercentage5 = task5.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
                () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
                () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0),
                () -> assertEquals(100.0, progressPercentage4),
                () -> assertEquals(0.0, progressPercentage5));
    }

    @Test
    void shouldUpdateAllTasksProgress() {
        scheduledTaskProgressService.updateTasksProgress();
        double progressPercentage1 = task1.getProgressPercentage();
        double progressPercentage2 = task2.getProgressPercentage();
        double progressPercentage3 = task3.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
                () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
                () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0));
    }
}