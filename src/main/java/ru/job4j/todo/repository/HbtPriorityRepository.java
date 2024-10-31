package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class HbtPriorityRepository implements PriorityStore {
    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(HbtPriorityRepository.class.getName());

    @Override
    public Optional<Priority> findById(int id) {
        try {
            return crudRepository.optional(
                    "from Priority where id = :fId", Priority.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Priority> findAll() {
        try {
            return crudRepository.query("FROM Priority", Priority.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
