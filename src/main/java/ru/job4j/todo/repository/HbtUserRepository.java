package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbtUserRepository implements UserStore {

    private final CrudRepository crudRepository;

    private static final Logger LOG = LoggerFactory.getLogger(HbtUserRepository.class.getName());

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            return crudRepository.optional(
                    "from User where id = :fId", User.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run(
                    "delete from User where id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return findById(id).isEmpty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crudRepository.optional(
                    "FROM User as user WHERE user.login = :userLogin AND user.password = :userPassword", User.class,
                    Map.of("userLogin", login, "userPassword", password)
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> findAll() {
        try {
            return crudRepository.query("from User", User.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
