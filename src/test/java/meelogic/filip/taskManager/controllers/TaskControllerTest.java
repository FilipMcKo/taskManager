package meelogic.filip.taskmanager.controllers;

import meelogic.filip.taskmanager.entities.external.TaskCreationRequest;
import meelogic.filip.taskmanager.entities.external.TaskDTO;
import meelogic.filip.taskmanager.entities.internal.State;
import meelogic.filip.taskmanager.entities.internal.Task;
import meelogic.filip.taskmanager.entities.internal.TaskPriority;
import meelogic.filip.taskmanager.services.exceptions.EntityDoesNotExistServiceException;
import meelogic.filip.taskmanager.services.exceptions.ForbiddenOperationServiceException;
import meelogic.filip.taskmanager.services.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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
        TaskCreationRequest taskCreationRequest1 = new TaskCreationRequest("getAllTestTask1", "shouldGetAllTasks", 100L,
            "HIGH");
        TaskCreationRequest taskCreationRequest2 = new TaskCreationRequest("getAllTestTask2", "shouldGetAllTasks", 100L,
            "LOW");

        //when
        Integer id1 = taskController.addNewTask(taskCreationRequest1).getBody().getId();
        Integer id2 = taskController.addNewTask(taskCreationRequest2).getBody().getId();
        TaskDTO taskDTO1 = new TaskDTO(id1, taskCreationRequest1.getName(), taskCreationRequest1.getDescription(),
            State.NEW, 0.0, "HIGH");
        TaskDTO taskDTO2 = new TaskDTO(id2, taskCreationRequest2.getName(), taskCreationRequest2.getDescription(),
            State.NEW, 0.0, "HIGH");
        List<TaskDTO> taskDTOList = taskController.getAllTasks();

        //then
        assertAll(() -> assertTrue(taskDTOList.stream().anyMatch(taskDTO -> taskDTO.getId().equals(taskDTO1.getId()))),
            () -> assertTrue(taskDTOList.stream().anyMatch(taskDTO -> taskDTO.getId().equals(taskDTO2.getId()))));

        taskController.removeTaskById(id1);
        taskController.removeTaskById(id2);
    }

    @Test
    void shouldGetTaskById() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("getTestTask", "shouldGetTaskById", 100L,
            "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        TaskDTO taskDTO = taskController.getTaskById(id);

        //then
        assertAll(() -> assertEquals(taskCreationRequest.getName(), taskDTO.getName()),
            () ->assertEquals(taskDTO.getDescription(), taskDTO.getDescription()));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldDeleteTaskById() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("deleteTestTask", "shouldDeleteTaskById",
            100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        assertNotNull(taskController.getTaskById(id));

        //then
        taskController.removeTaskById(id);
        assertThrows(EntityDoesNotExistException.class, () -> taskController.getTaskById(id));
    }

    @Test
    void shouldCreateNewTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("createNewTestTask", "shouldCreateNewTask",
            100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();

        //then
        assertNotNull(taskController.getTaskById(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldRenameTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("renameTestTask", "shouldRenameTask", 100L,
            "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.renameTaskById(id, "renamedTask");

        //then
        assertEquals("renamedTask", taskController.getTaskById(id).getName());
        taskController.removeTaskById(id);
    }

    @Test
    void shouldPutTaskOnQueue() throws InterruptedException {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask", "shouldPutTaskOnQueue",
            2000L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.startProcessingTask(id);

        //then
        assertEquals(State.PENDING, taskController.getTaskById(id).getCurrentState());
        for (int i = 0; i < 5; i++) {
            if (taskController.getTaskById(id).getCurrentState() != State.PENDING) {
                break;
            }
            Thread.sleep(500);
        }
        assertEquals(State.RUNNING, taskController.getTaskById(id).getCurrentState());
        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToStartRunningTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask",
            "shouldntAllowToStartRunningTask", 100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.startProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.startProcessingTask(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToStartCancelledTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask",
            "shouldtAllowToStartCancelledTask", 100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.startProcessingTask(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToStartFinishedTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("startTestTask",
            "shouldtAllowToStartFinishedTask", 100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
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
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask", "shouldCancellTask", 100L,
            "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertEquals(State.CANCELLED, taskController.getTaskById(id).getCurrentState());
        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToCancelNewTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask",
            "shouldntAllowToCancelNewTask", 100L, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.cancelProcessingTask(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldntAllowToCancelCancelledTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask",
            "shouldntAllowToCancelCancelledTask", 1000l, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.startProcessingTask(id);
        taskController.cancelProcessingTask(id);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.cancelProcessingTask(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldtAllowToCancelFinishedTask() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask",
            "shouldtAllowToCancelFinishedTask", 1000l, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        Task task = taskRepository.findById(id).get();
        task.setCurrentState(State.FINISHED);
        taskRepository.save(task);

        //then
        assertThrows(ForbiddenOperationServiceException.class, () -> taskController.cancelProcessingTask(id));
        taskController.removeTaskById(id);
    }

    @Test
    void shouldThrowEntityDoesNotExistExceptionForAllOperations() {
        //given
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("cancelTestTask",
            "shouldtAllowToCancelFinishedTask", 1000l, "HIGH");

        //when
        Integer id = taskController.addNewTask(taskCreationRequest).getBody().getId();
        taskController.removeTaskById(id);

        //then
        assertAll(() -> assertThrows(EntityDoesNotExistException.class, () -> taskController.getTaskById(id)),
            () ->assertThrows(EntityDoesNotExistServiceException.class, () -> taskController.removeTaskById(id)),
            () -> assertThrows(EntityDoesNotExistServiceException.class,
                () -> taskController.renameTaskById(id, "newName")),
            () -> assertThrows(EntityDoesNotExistServiceException.class, () -> taskController.startProcessingTask(id)),
            () -> assertThrows(EntityDoesNotExistServiceException.class,
                () -> taskController.cancelProcessingTask(id)));
    }

    @Test
    void shouldFinishTasksByPriority() {
        //given
        int testCapacity = 12;
        int prioritiesRange = 2;
        List<TaskCreationRequest> taskCreationRequests = new LinkedList<>();
        List<Integer> taskId = new LinkedList<>();

        for (int i = 0; i < testCapacity; i++) {
            int value = ThreadLocalRandom.current().nextInt(0, prioritiesRange);
            TaskPriority taskPriority = TaskPriority.values()[value];
            taskCreationRequests.add(
                new TaskCreationRequest("byPriority1", "shouldFinishTasksByPriority", 3000L, taskPriority.toString()));
        }

        for (int i = 0; i < testCapacity; i++) {
            taskId.add(taskController.addNewTask(taskCreationRequests.get(i)).getBody().getId());
        }

        //when
        taskId.forEach(id -> taskController.startProcessingTask(id));

        List<Integer> runningTasksExpected = new ArrayList<>(
            Arrays.asList(taskId.get(0), taskId.get(1), taskId.get(2), taskId.get(3), taskId.get(4)));
        List<Integer> runningTasksActual = new ArrayList<>(
            Arrays.asList(taskId.get(0), taskId.get(1), taskId.get(2), taskId.get(3), taskId.get(4)));

        for (int i = 5; i < testCapacity; i++) {
            if (taskCreationRequests.get(i).getTaskPriority().equals("HIGH")) {
                runningTasksExpected.add(taskId.get(i));
            }
        }
        for (int i = 5; i < testCapacity; i++) {
            if (taskCreationRequests.get(i).getTaskPriority().equals("LOW")) {
                runningTasksExpected.add(taskId.get(i));
            }
        }
        List<Integer> taskIdCopy = new ArrayList<>(taskId);
        taskId.removeAll(runningTasksActual);

        //then
        while (!taskId.isEmpty()) {
            taskId.forEach(id -> {
                if (taskController.getTaskById(id).getCurrentState() == State.RUNNING) {
                    runningTasksActual.add(id);
                }
            });
            taskId.removeIf(runningTasksActual::contains);
        }

        taskIdCopy.forEach(id -> taskController.removeTaskById(id));
        assertEquals(runningTasksExpected, runningTasksActual);
    }
}