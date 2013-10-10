package db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/7/13
 */
public class DBSessionFactory {
    private static final Log logger = LogFactory.getLog(DBSessionFactory.class);
    private static volatile DBSessionFactory instance;
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    private DBSessionFactory() throws SQLException {
        buildSessionFactory();
    }

    public static DBSessionFactory getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBSessionFactory.class) {
                if (instance == null) {
                    instance = new DBSessionFactory();
                }
            }
        }
        return instance;
    }

    protected static void buildSessionFactory() {
        Configuration configuration = new Configuration();
        logger.trace("Create new Configuration with our properties!");
        configuration.configure();
        logger.trace("configure the Configuration with our properties!");
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        logger.trace("create serviceRegistry with our configuration!");
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        logger.trace("create sessionFactory with our serviceRegistry!");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}