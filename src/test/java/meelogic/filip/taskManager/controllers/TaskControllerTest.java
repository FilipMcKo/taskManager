package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.external.TaskDTO;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.services.exceptions.ForbiddenOperationServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskController taskController;

    @Test
    void shouldLoadContext() {
        assertNotNull(taskController);
    }

    @Test
    void shouldGetAllTasks() {
        //given
        TaskCreationRequest taskCreationRequest1 = new TaskCreationRequest("getAllTestTask1", "shouldGetAllTasks");
        TaskCreationRequest taskCreationRequest2 = new TaskCreationRequest("getAllTestTask2", "shouldGetAllTasks");

        //when
        Integer id1 = Integer.parseInt(taskController.addNewTask(taskCreationRequest1).getBody());
        Integer id2 = Integer.parseInt(taskController.addNewTask(taskCreationRequest2).getBody());
        TaskDTO taskDTO1 = new TaskDTO(id1, taskCreationRequest1.getName(), taskCreationRequest1.getDecription(), State.NEW, 0.0);
        TaskDTO taskDTO2 = new TaskDTO(id2, taskCreationRequest2.getName(), taskCreationRequest2.getDecription(), State.NEW, 0.0);
        List<TaskDTO> taskDTOList = taskController.getAllTasks();

        //then
        assertAll(() -> assertTrue(taskDTOList.contains(taskDTO1)),
                () -> assertTrue(taskDTOList.contains(taskDTO2)));

        taskController.removeTaskById(id1);
        taskController.removeTaskById(id2);
    }

    @Test
    void shouldGetTaskById() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("getTestTask", "shouldGetTaskById");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        TaskDTO taskDTO = taskController.getTaskById(id).getBody();

        //then
        assertAll(() -> assertEquals(taskCreationRequest.getName(), taskDTO.getName()),
                () -> assertEquals(taskDTO.getDescription(), taskDTO.getDescription()));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldDeleteTaskById() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("deleteTestTask", "shouldDeleteTaskById");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        assertNotNull(taskController.getTaskById(id));

        //then
        taskController.removeTaskById(id);
        assertNull(taskController.getTaskById(id).getBody());
    }

    @Test
    void shouldCreateNewTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("createNewTestTask", "shouldCreateNewTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());

        //then
        assertNotNull(taskController.getTaskById(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldRenameTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("renameTestTask", "shouldRenameTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.renameTaskById(id, "renamedTask");

        //then
        assertEquals("renamedTask", taskController.getTaskById(id).getBody().getName());

        taskController.removeTaskById(id);
    }

    @Test
    void shouldStartTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask", "shouldStartTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.startProcessingTask(id);

        //then
        assertEquals(State.RUNNING, taskController.getTaskById(id).getBody().getCurrentState());

        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToStartRunningTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask", "shouldntAllowToStartRunningTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.startProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.startProcessingTask(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToStartCancelledTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask", "shouldtAllowToStartCancelledTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.startProcessingTask(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToStartFinishedTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask", "shouldtAllowToStartFinishedTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        Task task = taskRepository.findById(id).get();
        task.setCurrentState(State.FINISHED);
        taskRepository.save(task);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.startProcessingTask(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldCancellTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask", "shouldCancellTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertEquals(State.CANCELLED, taskController.getTaskById(id).getBody().getCurrentState());

        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToCancelNewTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask", "shouldntAllowToCancelNewTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());

        //then
        assertThrows(ForbiddenOperationException.class, () -> taskController.cancelProcessingTask(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToCancelCancelledTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask", "shouldntAllowToCancelCancelledTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationException.class, () -> taskController.cancelProcessingTask(id));

        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToCancelFinishedTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask", "shouldtAllowToCancelFinishedTask");

        //when
        Integer id = Integer.parseInt(taskController.addNewTask(taskCreationRequest).getBody());
        Task task = taskRepository.findById(id).get();
        task.setCurrentState(State.FINISHED);
        taskRepository.save(task);

        //then
        assertThrows(ForbiddenOperationException.class, () -> taskController.cancelProcessingTask(id));

        taskController.removeTaskById(id);
    }
}