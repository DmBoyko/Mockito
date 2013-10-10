package db;


public interface RoleDao {
    public void create(Role role) throws NotUniqueRoleNameException, DBSystemException;

    public void update(Role role) throws DBSystemException;

    public void remove(Role role) throws DBSystemException;

    public Role findByName(String name);
}
