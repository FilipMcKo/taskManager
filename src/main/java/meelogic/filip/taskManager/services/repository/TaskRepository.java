package meelogic.filip.taskmanager.services.repository;

import meelogic.filip.taskmanager.entities.internal.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
