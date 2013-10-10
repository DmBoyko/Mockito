package service;

import db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/9/13
 */
@Service
public class UserServiceImpl implements UserService {

    @Qualifier("DBUserDao")
    @Autowired
    private UserDao userDao;

    @Override
    public void create(User user) throws NotUniqueEmailException, NotUniqueLoginException, DBSystemException, NotUniqueRoleNameException {
        userDao.create(user);
    }

    @Override
    public void update(User user) throws DBSystemException, NotUniqueRoleNameException, NotUniqueEmailException {
        userDao.update(user);
    }

    @Override
    public void remove(User user) throws DBSystemException {
        userDao.remove(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
