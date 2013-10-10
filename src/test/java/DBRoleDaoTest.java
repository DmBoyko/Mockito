import db.*;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.RoleService;
import service.RoleServiceImpl;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import java.io.InputStream;

public class DBRoleDaoTest extends DBTestCase {
    private static String propertyPath = "hibernate.cfg.xml";
    private static final String dataSetXML = "dataset.xml";
    private static final String[] LOCATIONS = {"spring-config_Test.xml"};
    private static InputStream dataSetInputStream;
    private static String DB_DRIVER_CLASS;
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    @Autowired
    private static SessionFactory sessionFactory;
    @Autowired
    private static RoleDao roleDao;

    static {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(LOCATIONS);
    }

    //
    public DBRoleDaoTest() {
        super();
        initProperties();
    }

    public DBRoleDaoTest(String name) {
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
    @After
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
    public void testFindByName() throws Exception {
        try {
            roleDao.findByName("");
            fail("Role should be checked for correct name argument before looking");
        } catch (IllegalArgumentException ex) {
        }
        Role role = roleDao.findByName("Admin");
        assertNotNull(role);
        assertEquals(role.getName(), "Admin");
        role = roleDao.findByName("wrongname");
        assertNull(role.getName());
    }

    @Test
    public void testCreate() throws Exception {
        Role role = null;
        try {
            roleDao.create(role);
            fail("Role should be checked for null before creating");
        } catch (NullPointerException npe) {
        }
        role = new Role();
        role.setName("Admin");
        try {
            roleDao.create(role);
            fail("Role should be checked for duplicating before creating");
        } catch (IllegalArgumentException npe) {
            fail("Role should be checked for duplicating before creating");
        } catch (NotUniqueRoleNameException e) {
        }
        role.setName("created");
        roleDao.create(role);
        Role newRole = roleDao.findByName("created");
        assertNotNull(newRole);
        assertEquals(newRole.getName(), "created");
    }


    @Test
    public void testUpdate() throws Exception {
        Role role = null;
        try {
            roleDao.update(role);
            fail("Role should be checked for null before updating");
        } catch (IllegalArgumentException ignore) {
        }
        role = new Role();
        try {
            roleDao.update(role);
            fail("Role should be checked for null ID before updating");
        } catch (IllegalArgumentException ignore) {
        }
        role = roleDao.findByName("USER");
        role.setName("newguest");
        roleDao.update(role);
        Role newRole = roleDao.findByName("USER1");
        assertNull(newRole.getName());
        newRole = roleDao.findByName("newguest");
        assertNotNull(newRole);
        assertTrue("Name of role should be not null", newRole.getName().intern() == "newguest");
    }

    @Test
    public void testRemove() throws Exception {
        Role role = null;
        try {
            roleDao.remove(role);
            fail("Role should be checked for null before removing");
        } catch (NullPointerException npe) {
        }
        role = roleDao.findByName("USER");
        roleDao.remove(role);
        Role newRole = roleDao.findByName("USER");
        assertNull(newRole.getName());
    }

    @BeforeClass
    public static void setRoleDao(RoleDao roleDaoTest) {
        roleDao = roleDaoTest;
    }

    @BeforeClass
    public static void setSessionFactory(SessionFactory sessionFactoryTest) {
        sessionFactory = sessionFactoryTest;
    }
}
