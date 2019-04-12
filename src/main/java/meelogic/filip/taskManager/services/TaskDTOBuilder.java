package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.entities.TaskDTO;

public class TaskDTOBuilder {

    public static TaskDTO taskToTaskDTO(Task task) {
        return new TaskDTO(task.getId(), task.getName(), task.getDescription(), task.getCurrentState(), task.getProgressPercentage());
    }
}
