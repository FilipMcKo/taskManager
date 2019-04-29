package meelogic.filip.taskManager.services.repository;

import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
}
