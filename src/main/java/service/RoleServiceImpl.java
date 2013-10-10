package service;

import db.DBSystemException;
import db.NotUniqueRoleNameException;
import db.Role;
import db.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/9/13
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Transactional
    public void create(Role role) throws NotUniqueRoleNameException, DBSystemException {
        roleDao.create(role);
    }

    @Transactional
    public void update(Role role) throws DBSystemException {
        roleDao.update(role);
    }

    @Transactional
    public void remove(Role role) throws DBSystemException {
        roleDao.remove(role);
    }

    @Transactional
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
