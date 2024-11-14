package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbtTaskRepository implements TaskStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(HbtTaskRepository.class.getName());

    @Override
    public Optional<Task> save(Task task) {
        try {
            crudRepository.run(session -> session.persist(task));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.of(task);
    }

    @Override
    public boolean deleteById(int id) {
        try {
        crudRepository.run(
                "delete from Task where id = :fId",
                Map.of("fId", id)
        );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public boolean update(Task task) {
        try {
            crudRepository.run(session -> session.merge(task));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        var updatedTask = findById(task.getId()).get();
        return  (task.getTitle().equals(updatedTask.getTitle())
                && task.getDescription().equals(updatedTask.getDescription()));
    }

    @Override
    public Optional<Task> findById(int id) {
        try {
            return crudRepository.optional(
                    "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.priority LEFT JOIN FETCH t.categories where t.id = :fId", Task.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        try {
            var tasks = crudRepository.query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.priority "
                    + "LEFT JOIN FETCH t.categories", Task.class);
            return tasks;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public List<Task> findAllDone() {
        try {
            return crudRepository.query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.priority "
                    + "LEFT JOIN FETCH t.categories WHERE t.done = true", Task.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> findNewTasks() {
        List<Task> result = new ArrayList<>();
        try {
            var allTasks = crudRepository.query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.priority "
                    + "LEFT JOIN FETCH t.categories", Task.class);
            var today = Timestamp.valueOf(LocalDateTime.now()).toString().substring(0, 10);
            for (Task task : allTasks) {
                if (task.getCreated().toString().startsWith(today)) {
                    result.add(task);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
    @Override
    public boolean setDoneTrue(int id) {
        var task = findById(id).get();
        task.setDone(true);
        return update(task);
    }
}
