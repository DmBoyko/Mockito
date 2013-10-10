package service;

import db.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/9/13
 */
public interface UserService {
    public void create(User user) throws DBSystemException, NotUniqueLoginException, NotUniqueEmailException, NotUniqueRoleNameException;

    public void update(User user) throws DBSystemException, NotUniqueRoleNameException, NotUniqueEmailException;

    public void remove(User user) throws DBSystemException;

    public List<User> findAll();

    public User findByLogin(String login);

    public User findByEmail(String email);
}
