package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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
    private HttpEntity entity = new HttpEntity(null);
    private Task sampleTask = new Task(1, "sampleTask", "task if database empty", State.NONE, 0.0, null);

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @BeforeEach
    void setUp() {
        if (taskRepository.getTaskList().get(0) == null) {
            taskRepository.create(sampleTask);
        }
    }

    @Test
    void getTaskDTONyIdTest() {
        ResponseEntity<String> response1 = restTemplate
                .exchange(createURLWithPort("/tasks/" + taskRepository.getTaskList().get(0).getId()), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/tasks/-1"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void deleteTask() {
        ResponseEntity<String> response1 = restTemplate
                .exchange(createURLWithPort("/tasks/" + taskRepository.getTaskList().get(0).getId()), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/tasks/-1"), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }


}