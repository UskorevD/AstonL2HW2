package config;

import entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration =
                    new Configuration().configure("hibernate.cfg.xml");

            Configuration localConfiguration =
                    new Configuration().configure("hibernate-local.cfg.xml");

            String username = localConfiguration.getProperties()
                    .getProperty("hibernate.connection.username");
            String password = localConfiguration.getProperties()
                    .getProperty("hibernate.connection.password");

            configuration.setProperty(
                    "hibernate.connection.username", username);
            configuration.setProperty(
                    "hibernate.connection.password", password);

            configuration.addAnnotatedClass(User.class);

            return configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (!SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}