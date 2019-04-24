package meelogic.filip.taskManager.entities.repository;

import meelogic.filip.taskManager.entities.internal.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    @Override
    <S extends Task> S save(S s);

    @Override
    Optional<Task> findById(Integer integer);

    @Override
    Iterable<Task> findAll();

    @Override
    void deleteById(Integer integer);
}
