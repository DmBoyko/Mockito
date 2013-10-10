package db;

import java.util.List;

public interface UserDao {
    public void create(User user) throws DBSystemException, NotUniqueLoginException, NotUniqueEmailException, NotUniqueRoleNameException;

    public void update(User user) throws DBSystemException, NotUniqueRoleNameException, NotUniqueEmailException;

    public void remove(User user) throws DBSystemException;

    public List<User> findAll();

    public User findByLogin(String login);

    public User findByEmail(String email);
}
