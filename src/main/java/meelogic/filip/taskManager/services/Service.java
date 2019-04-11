package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.entities.TaskDTO;
import meelogic.filip.taskManager.entities.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Service {

    @Autowired
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> taskDTOList = new LinkedList<>();
        List<Task> taskList = TaskRepository.getTaskList();
        for (Task task : taskList) {
            taskDTOList.add(TaskDTOBuilder.taskToTaskDTO(task));
        }
        return taskDTOList;
    }
}
