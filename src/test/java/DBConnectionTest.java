import db.*;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
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

public class DBConnectionTest extends DBTestCase {
    private static final String propertyPath = "hibernate.cfg.xml";
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
    private static UserDao userDao;
    @Autowired
    private static RoleDao roleDao;

    @BeforeClass
    public static void setSessionFactory(SessionFactory sessionFactory) {
        DBConnectionTest.sessionFactory = sessionFactory;
    }

    @BeforeClass
    public static void setUserDao(UserDao userDao) {
        DBConnectionTest.userDao = userDao;
    }

    @BeforeClass
    public static void setRoleDao(RoleDao roleDao) {
        DBConnectionTest.roleDao = roleDao;
    }

    static {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(LOCATIONS);
    }

    public DBConnectionTest() {
    }

    public DBConnectionTest(String name) {
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

    @Test
    public void testDBTableUser() throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("USER");

        dataSetInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetXML);
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(dataSetInputStream);
        ITable expectedTable = expectedDataSet.getTable("USER");
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void testDBTableRole() throws Exception {
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("ROLE");

        dataSetInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetXML);
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(dataSetInputStream);
        ITable expectedTable = expectedDataSet.getTable("ROLE");
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredActualTable);
    }

    @Test
    public void testDBTableUserCreate() throws Exception {
        User user = new User();
        user.setLogin("dboyko");
        user.setEmail("test_dboyko@mail.com");
        Role roleAdmin = roleDao.findByName("Admin");
        user.setFirstName("Dima");
        user.setLastName("Boyko");
        user.setPassword("123456");
        user.setRole(roleAdmin);
        userDao.create(user);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("USER");
        assertEquals(4, actualTable.getRowCount());
    }

    @Test
    public void testDBTableUserDelete() throws Exception {
        User user = new User();
        user.setLogin("dboyko");
        user.setEmail("test_dboyko@mail.com");
        Role roleAdmin = roleDao.findByName("Admin");
        user.setLastName("Boyko");
        user.setFirstName("Dmitry");
        user.setPassword("123456");
        user.setRole(roleAdmin);
        userDao.create(user);
        userDao.remove(user);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("USER");
        assertEquals(3, actualTable.getRowCount());
    }

    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }
}
