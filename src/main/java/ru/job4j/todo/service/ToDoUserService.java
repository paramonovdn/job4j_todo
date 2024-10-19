package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserStore;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoUserService implements UserService{

    private final UserStore userStore;
    @Override
    public Optional<User> save(User user) {
        return userStore.save(user);
    }

    @Override
    public boolean deleteById(int id) {
        return userStore.deleteById(id);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userStore.findByLoginAndPassword(login, password);
    }

    @Override
    public Collection<User> findAll() {
        return userStore.findAll();
    }
}
