package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore implements Store {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(TaskStore.class.getName());

    @Override
    public Optional<Task> save(Task task) {
        var session = sf.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            session.save(task);
            var newId = session.createQuery("from Task as task where task.title = :taskTitle AND"
                            + " task.description = :taskDescription AND "
                            + "task.created = :taskCreated AND task.done = :taskDone", Task.class)
                    .setParameter("taskTitle", task.getTitle())
                    .setParameter("taskDescription", task.getDescription())
                    .setParameter("taskCreated", task.getCreated())
                    .setParameter("taskDone", task.isDone())
                    .uniqueResult().getId();
            session.getTransaction().commit();
            task.setId(newId);
            result = Optional.ofNullable(task);
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        var session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            var query = session.createQuery("DELETE Task WHERE id = :tId")
                    .setParameter("tId", id);
            var affectedRows = query.executeUpdate();
            result = affectedRows > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean update(Task task) {
        var session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            var query = session.createQuery("UPDATE Task SET title = :tTitle, description = :tDescription, "
                            + "created = :tCreated, done = :tDone WHERE id = :tId")
                    .setParameter("tTitle", task.getTitle())
                    .setParameter("tDescription", task.getDescription())
                    .setParameter("tCreated", task.getCreated())
                    .setParameter("tDone", task.isDone())
                    .setParameter("tId", task.getId());
            var affectedRows = query.executeUpdate();
            result = affectedRows > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task as task where task.id = :taskId", Task.class);
            query.setParameter("taskId", id);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        var session = sf.openSession();
        List<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }
}
