package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbtUserRepository implements UserStore {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(HbtUserRepository.class.getName());


    @Override
    public Optional<User> save(User user) {
        var session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            session.save(user);
            result = Optional.ofNullable(user);
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
    public boolean deleteById(int id) {
        var session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            var query = session.createQuery("DELETE User WHERE id = :uId")
                    .setParameter("uId", id);
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
    public Optional<User> findByLoginAndPassword(String login, String password) {
        var session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User as user WHERE user.login = :userLogin AND user.password = :userPassword", User.class);
            query.setParameter("userLogin", login)
            .setParameter("userPassword", password);
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
    public Collection<User> findAll() {
        var session = sf.openSession();
        List<User> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("from User", User.class).list();
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
