package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private HibernateUtil() {}

    public static synchronized SessionFactory  getSessionFactory(){
        if(sessionFactory == null){
            try {
                logger.info("Initializing Hibernate SessionFactory");
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml").buildSessionFactory();
                logger.info("Hibernate SessionFactory initialized");
            }
            catch (Exception e){
                logger.error("Failed to initialize Hibernate SessionFactory.",e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory){
        HibernateUtil.sessionFactory = sessionFactory;
    }
}
