package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import meelogic.filip.taskManager.services.exceptions.TaskIsAlreadyFinishedException;
import meelogic.filip.taskManager.services.exceptions.TaskIsAlreadyRunningException;
import meelogic.filip.taskManager.services.exceptions.TaskWasAlreadyCancelledException;
import meelogic.filip.taskManager.services.exceptions.TaskWasNeverStartedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class TaskStateServiceTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    @InjectMocks
    public TaskStateService taskStateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Task sampleTask = new Task(1, "Task2", "Sample task nr two", State.NEW, 0.0, null,false);
        Mockito.when(taskRepositoryMock.findById(sampleTask.getId())).thenReturn(java.util.Optional.of(sampleTask));
    }

    @Test
    void setUpStateTaskTest() {
        assertEquals(State.NEW, taskRepositoryMock.findById(1).get().getCurrentState());
        assertNull(taskRepositoryMock.findById(1).get().getTaskBeginTime());
    }

    @Test
    void startProcessingTest() {
        taskStateService.startProcessing(1);
        assertEquals(State.RUNNING, taskRepositoryMock.findById(1).get().getCurrentState());
        assertNotNull(taskRepositoryMock.findById(1).get().getTaskBeginTime());
    }


    @Test
    void startRunningTaskTest() {
        taskStateService.startProcessing(1);
        assertThrows(TaskIsAlreadyRunningException.class, () -> taskStateService.startProcessing(1));
    }

    @Test
    void startCancelledTaskTest() {
        taskStateService.startProcessing(1);
        taskStateService.cancelProcessing(1);
        taskStateService.startProcessing(1);
        assertEquals(State.RUNNING, taskRepositoryMock.findById(1).get().getCurrentState());
        assertNotNull(taskRepositoryMock.findById(1).get().getTaskBeginTime());

    }

    @Test
    void startFinishedTaskTest() {
        Task sampleTask = new Task(1, "Task2", "Sample task nr two", State.FINISHED, 0.0, null,false);
        Mockito.when(taskRepositoryMock.findById(sampleTask.getId())).thenReturn(java.util.Optional.of(sampleTask));
        assertThrows(TaskIsAlreadyFinishedException.class,()->taskStateService.startProcessing(1));
    }

    @Test
    void cancelSetUpStateTaskTest() {
       assertThrows(TaskWasNeverStartedException.class,()->taskStateService.cancelProcessing(1));
    }

    @Test
    void cancelProcessingTaskTest() {
        taskStateService.startProcessing(1);
        taskStateService.cancelProcessing(1);
        assertEquals(State.CANCELLED, taskRepositoryMock.findById(1).get().getCurrentState());
        assertNull(taskRepositoryMock.findById(1).get().getTaskBeginTime());
    }

    @Test
    void cancelCancelledTaskTest() {
        taskStateService.startProcessing(1);
        taskStateService.cancelProcessing(1);
        assertEquals(State.CANCELLED, taskRepositoryMock.findById(1).get().getCurrentState());
        assertNull(taskRepositoryMock.findById(1).get().getTaskBeginTime());
        assertThrows(TaskWasAlreadyCancelledException.class, ()->taskStateService.cancelProcessing(1));
    }

    @Test
    void cancelFinishedTaskTest() {
        Task sampleTask = new Task(1, "Task2", "Sample task nr two", State.FINISHED, 0.0, null,false);
        Mockito.when(taskRepositoryMock.findById(sampleTask.getId())).thenReturn(java.util.Optional.of(sampleTask));
        assertThrows(TaskIsAlreadyFinishedException.class,()->taskStateService.cancelProcessing(1));
    }
}