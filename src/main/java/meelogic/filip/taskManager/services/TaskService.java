package meelogic.filip.taskManager.services;

import meelogic.filip.taskManager.services.exceptions.OperationStatus;
import meelogic.filip.taskManager.services.repository.TaskRepository;
import meelogic.filip.taskManager.entities.external.TaskCreationRequest;
import meelogic.filip.taskManager.entities.internal.State;
import meelogic.filip.taskManager.entities.internal.Task;
import meelogic.filip.taskManager.services.exceptions.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Page<Task> getAllTasksPaged(Pageable pageable){
        return taskRepository.findAll(pageable);
    }

    public Optional<Task> getTaskById(Integer id) {
        return this.taskRepository.findById(id);
    }

    public void removeTaskById(Integer id) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), OperationStatus.ENTITY_NOT_FOUND);
        this.taskRepository.deleteById(id);
    }

    public Task addNewTask(TaskCreationRequest taskCreationRequest) {
        Task task = new Task();
        task.setName(taskCreationRequest.getName());
        task.setDescription(taskCreationRequest.getDescription());
        task.setCurrentState(State.NEW);
        task.setProgressPercentage(0.0);
        return this.taskRepository.save(task);
    }

    public void renameTaskById(Integer id, String newName) {
        Optional<Task> optTask = this.taskRepository.findById(id);
        Preconditions.checkArgument(optTask.isPresent(), OperationStatus.ENTITY_NOT_FOUND);
        Task task = optTask.get();
        task.setName(newName);
        this.taskRepository.save(task);
    }
}
