package db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/6/13
 */
@Repository
public class DBUserDao implements UserDao {

    @Qualifier("sessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public void create(User user) throws NotUniqueLoginException, NotUniqueEmailException {
        if (user == null) {
            throw new NullPointerException();
        }
        if (findByLogin(user.getLogin()).getLogin() != null) {
            throw new NotUniqueLoginException("The login of user not unique!" + user.getLogin());
        }
        if (findByEmail(user.getEmail()).getEmail() != null) {
            throw new NotUniqueEmailException("The email of user not unique!" + user.getEmail());
        }
        currentSession().save(user);
    }

    @Override
    @Transactional
    public void update(User user) throws NotUniqueEmailException {
        if (user == null) {
            throw new NullPointerException();
        }
        if (user.getId() < 1) {
            throw new IllegalArgumentException();
        }
        if (user.getEmail() == null || user.getEmail().intern() == "") {
            throw new IllegalArgumentException();
        }
        if (findByEmail(user.getEmail()).getEmail() != null) {
            throw new NotUniqueEmailException("The email of user not unique!" + user.getEmail());
        }
        currentSession().update(user);
    }

    @Override
    @Transactional
    public void remove(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        currentSession().delete(user);
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return currentSession().createCriteria(User.class).list();
    }

    @Override
    @Transactional
    public User findByLogin(String login) {
        if (login == null || login.intern() == "") {
            throw new IllegalArgumentException();
        }
        Criteria criteria = currentSession().createCriteria(User.class)
                .add(Restrictions.like("login", login));
        if (criteria.list().isEmpty()) {
            return new User();
        }
        return (User) criteria.list().get(0);
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        if (email == null || email.intern() == "") {
            throw new IllegalArgumentException();
        }
        Criteria criteria = currentSession().createCriteria(User.class)
                .add(Restrictions.like("email", email));
        if (criteria.list().isEmpty()) {
            return new User();
        }
        return (User) criteria.list().get(0);
    }
}
