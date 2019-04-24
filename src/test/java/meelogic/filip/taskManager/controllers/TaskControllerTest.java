package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private Task sampleTask = new Task(1, "sampleTask", "task if database empty", State.NEW, 0.0, null,false);
    private HttpEntity entity = new HttpEntity(sampleTask);

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @BeforeEach
    void setUp() {
        if (!taskRepository.findAll().iterator().hasNext()) {
            taskRepository.save(sampleTask);
        }
    }

    @Test
    void getAllTasksTest() {
        ResponseEntity<String> response = restTemplate
                .getForEntity(createURLWithPort("/tasks"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTaskDTONyIdTest() {
        ResponseEntity<String> response1 = restTemplate
                .getForEntity(createURLWithPort("/tasks/" + taskRepository.findAll().iterator().next().getId()), String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate
                .getForEntity(createURLWithPort("/tasks/-1"), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void deleteTaskTask() {
        ResponseEntity<String> response1 = restTemplate
                .exchange(createURLWithPort("/tasks/" + taskRepository.findAll().iterator().next().getId()), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate
                .exchange(createURLWithPort("/tasks/-1"), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void newTaskTest() {
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/tasks"), sampleTask, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void renameTaskTest() {
        ResponseEntity<String> response1 = restTemplate
                .exchange(createURLWithPort("/tasks/") + taskRepository.findAll().iterator().next().getId() + "/rename", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate
                .exchange(createURLWithPort("/tasks/-1/rename"), HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void startTaskTest() {
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/tasks/-1/start"), HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cancelTaskTest() {
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("/tasks/-1/cancel"), HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}