package meelogic.filip.taskManager.controllers;

import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.entities.repository.TaskRepository;
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
    TaskRepository taskRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    void getTaskDTONyIdTest() throws Exception {
        HttpEntity entity = new HttpEntity(null);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/tasks/1"), HttpMethod.GET, entity, String.class);
        Task task = taskRepository.read(1);
        String expected = "{\"id\":1,\"name\":\"Task1\",\"description\":\"Sample task nr one\",\"currentState\":\"NONE\",\"progressPercentage\":0.0}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    void deleteTask() {
        HttpEntity entity = new HttpEntity(null);
        restTemplate.delete(createURLWithPort("/tasks/1"));
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/tasks/1"), HttpMethod.GET, entity, String.class);
        assertEquals(0, response.getHeaders().getContentLength());
    }

    /*@Test
    void createNewTaskTest(){
        HttpEntity entity = new HttpEntity(null);
        Task task = new Task();
        task.setName("dasd");
        task.setDescription("asdasdasdasd");
        restTemplate.postForEntity(createURLWithPort("/tasks/newtask"), HttpMethod.POST,entity,task);
    }*/
}