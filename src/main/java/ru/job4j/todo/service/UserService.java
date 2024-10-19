package ru.job4j.todo.service;

import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> save(User user);

    boolean deleteById(int id);

    Optional<User> findByLoginAndPassword(String login, String password);

    Collection<User> findAll();
}
