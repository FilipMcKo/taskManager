package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Integer id) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), HttpStatus.NOT_FOUND);
        return optTask.get();
    }

    public void removeTaskById(Integer id) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), HttpStatus.NOT_FOUND);
        this.taskRepository.deleteById(id);
    }

    public Task addNewTask(TaskCreationRequest taskCreationRequest) {
        Task task = new Task();
        task.setName(taskCreationRequest.getName());
        task.setDescription(taskCreationRequest.getDecription());
        task.setCurrentState(State.NEW);
        task.setProgressPercentage(0.0);
        this.taskRepository.save(task);
        return task;
    }

    public void renameTaskById(Integer id, String newName) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), HttpStatus.NOT_FOUND);
        Task task = optTask.get();
        task.setName(newName);
        this.taskRepository.save(task);
    }
}
