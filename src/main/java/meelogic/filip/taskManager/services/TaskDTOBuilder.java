package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.entities.Task;
import meelogic.filip.taskManager.entities.TaskDTO;

import java.util.HashMap;
import java.util.Map;

public class TaskDTOBuilder {

   public static TaskDTO taskToTaskDTO(Task task){
       return new TaskDTO(task.getId(),task.getName(),task.getCurrentState(),task.getProgressPercentage(),task.getTaskBeginTime());
   }

}
