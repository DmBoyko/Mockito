package service;

import db.DBSystemException;
import db.NotUniqueRoleNameException;
import db.Role;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/9/13
 */
public interface RoleService {
    public void create(Role role) throws NotUniqueRoleNameException, DBSystemException;

    public void update(Role role) throws DBSystemException;

    public void remove(Role role) throws DBSystemException;

    public Role findByName(String name);
}
