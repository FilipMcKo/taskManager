package meelogic.filip.taskManager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository<Task, Integer> extends JpaRepository<Task, Integer> {
}
