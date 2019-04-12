package meelogic.filip.taskManager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import meelogic.filip.taskManager.entities.Task;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Deprecated
public class JsonTaskParser {

    private static final String FILE_PATH = "sampleTasks";

    public Map<Integer, Task> getSampleTaskMap() {
        Map<Integer, Task> taskMap = new HashMap<>();
        List<Task> taskList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File tasksFile = new File(FILE_PATH);
        try {
            taskList = Arrays.asList(mapper.readValue(tasksFile, Task[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).setId(i);
            taskMap.put(i, taskList.get(i));
        }

        return taskMap;
    }
}
