package meelogic.filip.taskManager.entities.repository;

import meelogic.filip.taskManager.entities.internal.Task;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

// TODO: spring-data
@Repository
public class TaskRepository {

    private Session session;

    public List<Task> getTaskList() {
        List<Task> taskList;
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        taskList = (List<Task>) session.createQuery("FROM task").list();
        session.close();
        return taskList;
    }

    public void create(Task task) {
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer id) {
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        Task task = session.get(Task.class, id);
        if (task == null) {
            throw new EntityNotFoundException("Unable to delete because such entity does not exist.");
        }
        session.delete(task);
        session.getTransaction().commit();
        session.close();
    }

    public Task read(Integer id) {
        this.session = HibernateUtils.getSessionFactory().openSession();
        this.session.beginTransaction();
        Task task;
        try {
            task = (Task) this.session.createQuery("FROM task WHERE id= :id").setParameter("id", id).getSingleResult();
        }catch(Exception e){
            throw new EntityNotFoundException("Unable to read because such entity does not exist.");
        }
        this.session.close();
        return task;
    }

    public void update(Task updatedTask) {
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        Task task1 = session.get(Task.class, updatedTask.getId());
        if (task1 == null) {
            throw new EntityNotFoundException("Unable to update because such entity does not exist.");
        }
        task1.setName(updatedTask.getName());
        task1.setDescription(updatedTask.getDescription());
        task1.setCurrentState(updatedTask.getCurrentState());
        task1.setProgressPercentage(updatedTask.getProgressPercentage());
        task1.setTaskBeginTime(updatedTask.getTaskBeginTime());
        session.getTransaction().commit();
        session.close();
    }

    public void findById(Integer id) {
    }
}
