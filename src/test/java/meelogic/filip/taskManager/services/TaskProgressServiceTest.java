package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.TaskRepository;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.*;

class TaskProgressServiceTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    @Mock
    private TaskRepository taskRepositoryMock;
    @InjectMocks
    private TaskProgressService taskProgressService;

    @BeforeEach
    void setUp() {
        task1 = new Task(1, "Task1", "Sample task nr one", State.NONE, 0.0, null);
        task2 = new Task(2, "Task2", "Sample task nr two", State.NONE, 0.0, null);
        task3 = new Task(3, "Task3", "Sample task nr three", State.NONE, 0.0, null);
        task4 = new Task(4, "Task4", "Sample task nr four", State.NONE, 0.0, null);
        task5 = new Task(4, "Task5", "Sample task nr five", State.NONE, 0.0, null);
        List<Task> taskList = new LinkedList<>(Arrays.asList(task1, task2, task3, task4, task5));
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskRepositoryMock.getTaskList()).thenReturn(taskList);
        doNothing().when(taskRepositoryMock).update(task1);
        doNothing().when(taskRepositoryMock).update(task2);
        doNothing().when(taskRepositoryMock).update(task3);
        doNothing().when(taskRepositoryMock).update(task4);
        doNothing().when(taskRepositoryMock).update(task5);
    }

    @Test
    void updateSingleTaskProgressTest() {
        task1.setTaskBeginTime(System.currentTimeMillis() - taskProgressService.getTaskDuration() / 2);
        taskProgressService.updateTaskProgress(task1);
        double progressPercentage1 = task1.getProgressPercentage();

        task2.setTaskBeginTime(System.currentTimeMillis() - taskProgressService.getTaskDuration() / 4);
        taskProgressService.updateTaskProgress(task2);
        double progressPercentage2 = task2.getProgressPercentage();

        task3.setTaskBeginTime(System.currentTimeMillis());
        taskProgressService.updateTaskProgress(task3);
        double progressPercentage3 = task3.getProgressPercentage();

        task4.setTaskBeginTime(System.currentTimeMillis() - 2 * taskProgressService.getTaskDuration());
        taskProgressService.updateTaskProgress(task4);
        double progressPercentage4 = task4.getProgressPercentage();

        double progressPercentage5 = task5.getProgressPercentage();
        assertAll(() -> assertEquals(50.0, progressPercentage1),
                () -> assertEquals(25.0, progressPercentage2),
                () -> assertEquals(0.0, progressPercentage3),
                () -> assertEquals(100.0, progressPercentage4),
                ()-> assertEquals(0.0, progressPercentage5));
    }

    @Test
    void updateAllTasksProgressTest(){

    }
}