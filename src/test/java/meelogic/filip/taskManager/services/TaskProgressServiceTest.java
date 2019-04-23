package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskProgressServiceTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    List<Task> taskList;
    @Mock
    private TaskRepository taskRepositoryMock;
    @InjectMocks
    private TaskProgressService taskProgressService;

    @BeforeEach
    void setUp() {
        task1 = new Task(1, "Task1", "Sample task nr one", State.NEW, 0.0, null);
        task2 = new Task(2, "Task2", "Sample task nr two", State.NEW, 0.0, null);
        task3 = new Task(3, "Task3", "Sample task nr three", State.NEW, 0.0, null);
        task4 = new Task(4, "Task4", "Sample task nr four", State.NEW, 0.0, null);
        task5 = new Task(4, "Task5", "Sample task nr five", State.NEW, 0.0, null);
        taskList = new LinkedList<>(Arrays.asList(task1, task2, task3));
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskRepositoryMock.getTaskList()).thenReturn(this.taskList);
        task1.setTaskBeginTime(System.currentTimeMillis() - taskProgressService.getTaskDuration() / 2);
        task2.setTaskBeginTime(System.currentTimeMillis() - taskProgressService.getTaskDuration() / 4);
        task3.setTaskBeginTime(System.currentTimeMillis());
        task4.setTaskBeginTime(System.currentTimeMillis() - 2 * taskProgressService.getTaskDuration());
    }

    @Test
    void updateSingleTaskProgressTest() {
        taskProgressService.updateTaskProgress(task1);
        double progressPercentage1 = task1.getProgressPercentage();

        taskProgressService.updateTaskProgress(task2);
        double progressPercentage2 = task2.getProgressPercentage();

        taskProgressService.updateTaskProgress(task3);
        double progressPercentage3 = task3.getProgressPercentage();

        taskProgressService.updateTaskProgress(task4);
        double progressPercentage4 = task4.getProgressPercentage();

        double progressPercentage5 = task5.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
                () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
                () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0),
                () -> assertEquals(100.0, progressPercentage4),
                () -> assertEquals(0.0, progressPercentage5));
    }

    @Test
    void updateAllTasksProgressTest() {
        taskProgressService.updateTasksProgress();
        double progressPercentage1 = task1.getProgressPercentage();
        double progressPercentage2 = task2.getProgressPercentage();
        double progressPercentage3 = task3.getProgressPercentage();

        assertAll(() -> assertTrue(50.0 <= progressPercentage1 && progressPercentage1 < 55.0),
                () -> assertTrue(25.0 <= progressPercentage2 && progressPercentage2 < 30.0),
                () -> assertTrue(0.0 <= progressPercentage3 && progressPercentage3 < 5.0));
    }
}