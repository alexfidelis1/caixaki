package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * @author alex - Responsavel por estabelecer a conexao com hibernate
 */

@ApplicationScoped
public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

	public static SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {

		try {
			if (sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}

			return sessionFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar conexao SessionFactory");
		}
	}

	/* Retorna o SessionFactory Corrente */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/* Retorna a session SessionFactory */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/* Abre uma nova sessão no SessionFactory */
	public static Session openSession() {
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		return sessionFactory.openSession();
	}

	/* Obtem a conexao do provedor de conexoes configurado */
	public static Connection getConnectionProvider() throws SQLException {
		return (Connection) ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}

	/* Retorna Connection no InitialContext java:/comp/env/jdbc/datasource */
	public static Connection getConnection() throws SQLException, NamingException {
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);

		return ds.getConnection();
	}

	/* Retorna Datasource JNDI tomcat */
	public DataSource getDataSourceJndi() throws NamingException {
		InitialContext context = new InitialContext();
		return (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}
}
