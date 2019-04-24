package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TaskServiceTest {
    private Task task1;
    private Task task2;
    private Task task3;
    List<Task> taskList;

    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private TaskProgressService taskProgressServiceMock;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        task1 = new Task(1, "Task1", "Sample task nr one", State.RUNNING, 3.3, 987987987L,false);
        task2 = new Task(2, "Task2", "Sample task nr two", State.NEW, 0.0, null,false);
        task3 = new Task(3, "Task3", "Sample task nr three", State.CANCELLED, 0.0, null,false);
        taskList = new LinkedList<>(Arrays.asList(task1, task2, task3));
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskRepositoryMock.findAll()).thenReturn(this.taskList);
        Mockito.when(taskRepositoryMock.findById(1)).thenReturn(java.util.Optional.ofNullable(this.task1));
    }

    @Test
    void getAllTaskDTOsTest() {
        List<TaskDTO> taskDTOList = this.taskService.getAllTasks();
        assertAll(() -> assertEquals(taskDTOList.get(0), new TaskDTO(task1.getId(), task1.getName(), task1.getDescription(), task1.getCurrentState(), task1.getProgressPercentage())),
                () -> assertEquals(taskDTOList.get(1), new TaskDTO(task2.getId(), task2.getName(), task2.getDescription(), task2.getCurrentState(), task2.getProgressPercentage())),
                () -> assertEquals(taskDTOList.get(2), new TaskDTO(task3.getId(), task3.getName(), task3.getDescription(), task3.getCurrentState(), task3.getProgressPercentage())));
        verify(taskProgressServiceMock, times(1)).updateTasksProgress();
    }

    @Test
    void getTaskDTObyIdTest() {
        TaskDTO taskDTO = taskService.getTaskById(1);
        assertEquals(taskDTO, new TaskDTO(task1.getId(), task1.getName(), task1.getDescription(), task1.getCurrentState(), task1.getProgressPercentage()));
        verify(taskProgressServiceMock, times(1)).updateTasksProgress();
    }

    @Test
    void removeTaskByIdVerified() {
        taskService.removeTaskById(1);
        taskService.removeTaskById(1);
        //verify(taskRepositoryMock, times(2)).delete(1);
    }

    @Test
    void addNewTask() {
        taskService.addNewTask(new TaskCreationRequest("newTask", "from task creator"));
        taskService.addNewTask(new TaskCreationRequest("newTask2", "also from task creator"));
        verify(taskRepositoryMock, times(1)).save(new Task(null, "newTask", "from task creator", State.NEW, 0.0, null,false));
        verify(taskRepositoryMock, times(1)).save(new Task(null, "newTask2", "also from task creator", State.NEW, 0.0, null,false));
    }

    @Test
    void renameTaskByIdTest() {
        taskService.renameTaskById(1, "New name");
        assertEquals("New name", task1.getName());

        taskService.renameTaskById(1, "");
        assertEquals("", task1.getName());
        verify(taskRepositoryMock, times(2)).save(task1);
    }
}