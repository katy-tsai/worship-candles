package tw.katy.com.util;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtils {

	private static SessionFactory sessionFactory = buidSessionFactory();
	private static ServiceRegistry serviceRegistry = null;

	public static SessionFactory buidSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.configure();

		Properties properties = configuration.getProperties();

		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(properties).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		return sessionFactory;
	}

	public Session opensession() {
		return sessionFactory.openSession();
	}

	public void closeSession(Session session) {
		session.close();
	}

	

}
