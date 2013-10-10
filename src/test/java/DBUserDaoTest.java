import db.*;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DBUserDaoTest extends DBTestCase {
    private static final String propertyPath = "hibernate.cfg.xml";
    private static final String dataSetXML = "dataset.xml";
    private static String DB_DRIVER_CLASS;
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static InputStream dataSetInputStream;
    private static final String[] LOCATIONS = {"spring-config_Test.xml"};
    private static final String SQL_SELECT_ALL_FROM_USER = "SELECT * from USER";

    @Autowired
    private static SessionFactory sessionFactory;
    @Autowired
    private static UserDao userDao;
    @Autowired
    private static RoleDao roleDao;


    @BeforeClass
    public static void setSessionFactory(SessionFactory sessionFactory) {
        DBUserDaoTest.sessionFactory = sessionFactory;
    }

    @BeforeClass
    public static void setUserDao(UserDao userDao) {
        DBUserDaoTest.userDao = userDao;
    }

    static {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(LOCATIONS);
    }

    public DBUserDaoTest() {
    }

    public DBUserDaoTest(String name) {
        super(name);
        initProperties();
    }

    private void initProperties() {
        Configuration configuration = new Configuration();
        configuration.configure(propertyPath);
        DB_PASSWORD = configuration.getProperties().getProperty("hibernate.connection.password");
        DB_USERNAME = configuration.getProperties().getProperty("hibernate.connection.username");
        DB_DRIVER_CLASS = configuration.getProperties().getProperty("hibernate.connection.driver_class");
        DB_URL = configuration.getProperties().getProperty("hibernate.connection.url");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, DB_DRIVER_CLASS);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_URL);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, DB_USERNAME);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, DB_PASSWORD);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        dataSetInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetXML);
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(dataSetInputStream);
        return dataSet;
    }

    @Override
    public void setUp() throws Exception {
        sessionFactory.openSession();
        IDatabaseConnection connection = getConnection();
        DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
    }

    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }

    @Test
    public void testCreate() throws NotUniqueEmailException, NotUniqueLoginException, DBSystemException, NotUniqueRoleNameException {
        User user = null;
        try {
            userDao.create(user);
            fail("User should be checked for null");
        } catch (NullPointerException ex) {
        }
        user = new User();
        user.setLogin("Sara");
        try {
            userDao.create(user);
            fail("User login should be checked for duplicating");
        } catch (NotUniqueLoginException e) {
        }
        user = new User();
        Role role = roleDao.findByName("Admin");
        user.setLogin("SaraTest");
        user.setEmail("test1@mail.com");
        user.setRole(role);
        try {
            userDao.create(user);
            fail("User email should be checked for duplicating");
        } catch (NotUniqueEmailException e) {
        }
        user.setLogin("new");
        user.setPassword("newpassw");
        user.setEmail("new@mail.com");
        user.setFirstName("Dmitry");
        user.setLastName("Boyko");
        assertNull("Name of User should be NULL in case when the user not found!", userDao.findByLogin("new").getLogin());
        userDao.create(user);
        assertNotNull(userDao.findByLogin("new"));
    }

    @Test
    public void testUpdate() throws DBSystemException, NotUniqueRoleNameException, NotUniqueEmailException {
        User user = null;
        try {
            userDao.update(user);
            fail("User should be checked for null");
        } catch (NullPointerException ignore) {
        }
        user = new User();
        try {
            userDao.update(user);
            fail("User ID should be checked for null before updating");
        } catch (IllegalArgumentException ignore) {
        }
        user.setId(-1);
        try {
            userDao.update(user);
            fail("User ID should be checked before updating");
        } catch (IllegalArgumentException ignore) {
        }
        user = userDao.findByLogin("Sara");
        user.setLogin("newLogin");
        user.setEmail("newEmail@mail.com");
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setPassword("newPSWD");
        Role role = roleDao.findByName("USER");
        user.setRole(role);
        userDao.update(user);
        assertEquals("newLogin", userDao.findByLogin("newLogin").getLogin());
        assertEquals("newEmail@mail.com", userDao.findByLogin("newLogin").getEmail());
        assertEquals("newFirstName", userDao.findByLogin("newLogin").getFirstName());
        assertEquals("newLastName", userDao.findByLogin("newLogin").getLastName());
        assertEquals("newPSWD", userDao.findByLogin("newLogin").getPassword());
        assertEquals("USER", userDao.findByLogin("newLogin").getRole().getName());
    }

    @Test
    public void testRemove() throws DBSystemException {
        User user = null;
        try {
            userDao.remove(user);
            fail("User should be checked for null");
        } catch (NullPointerException ex) {
        }
        user = userDao.findByLogin("Sara");
        assertNotNull(user.getLogin());
        userDao.remove(user);
        user = userDao.findByLogin("Sara");
        assertNull(user.getLogin());
    }

    @Test
    public void testFindByEmail() throws NotUniqueEmailException, NotUniqueLoginException, DBSystemException, NotUniqueRoleNameException {
        try {
            userDao.findByEmail(null);
            fail("Email should be checked for null before looking");
        } catch (IllegalArgumentException ex) {
        }
        try {
            userDao.findByEmail("");
            fail("Email should be checked for correct name before looking");
        } catch (IllegalArgumentException ex) {
        }
        User user = userDao.findByEmail("JdbcTest@mail.com");
        assertNull(user.getLogin());
        user = new User();
        user.setLogin("JdbcTest");
        user.setEmail("JdbcTest@mail.com");
        user.setLastName("Dmitry");
        user.setFirstName("Boyko");
        user.setPassword("123456");
        Role role = roleDao.findByName("Admin");
        user.setRole(role);
        userDao.create(user);
        assertNotNull(userDao.findByEmail("JdbcTest@mail.com").getLogin());
    }

    @Test
    public void testFindByLogin() throws NotUniqueEmailException, NotUniqueLoginException, DBSystemException, NotUniqueRoleNameException {
        try {
            userDao.findByLogin(null);
            fail("Login should be checked for null before looking");
        } catch (IllegalArgumentException ex) {
        }
        try {
            userDao.findByLogin("");
            fail("Login should be checked for correct name before looking");
        } catch (IllegalArgumentException ex) {
        }
        User user = userDao.findByLogin("JdbcTest");
        assertNull(user.getLogin());
        user = new User();
        user.setLogin("JdbcTest");
        user.setEmail("JdbcTest@mail.com");
        user.setFirstName("Dmitry");
        user.setLastName("Boyko");
        Role role = roleDao.findByName("Admin");
        user.setRole(role);
        user.setPassword("123456");
        userDao.create(user);
        assertNotNull(userDao.findByLogin("JdbcTest").getLogin());
    }

    @Test
    public void testFindAll() throws Exception {
        Connection con = getConnection().getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL_FROM_USER);
        ResultSet rs = ps.executeQuery();
        rs.last();
        int rowsQty = rs.getRow();
        List<User> userList = userDao.findAll();
        assertEquals(rowsQty, userList.size());
    }

    @BeforeClass
    public static void setRoleDao(RoleDao roleDaoTest) {
        roleDao = roleDaoTest;
    }
}
