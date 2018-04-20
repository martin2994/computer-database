package main.java.com.excilys.cdb.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.dao.impl.CompanyDAO;
import main.java.com.excilys.cdb.dao.impl.ComputerDAO;
import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;

/**
 * Fabrique de DAO Singleton retournant les DAO
 */
public class DAOFactory {

	/**
	 * La connexion à la BD
	 */
	private static Connection connection;

	/**
	 * Singleton de la fabrique
	 */
	private static DAOFactory factory;
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DAOFactory.class);

	/**
	 * Constructeur qui crée la connexion à la DB à partir d'un fichier de
	 * properties
	 * 
	 */
	private DAOFactory() {
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = getClass().getClassLoader().getResourceAsStream("main/resources/config.properties");
			prop.load(input);
			String database = prop.getProperty("database");
			String user = prop.getProperty("dbuser");
			String password = prop.getProperty("dbpassword");
			connection = DriverManager.getConnection(database, user, password);
		} catch (IOException | SQLException e) {
			logger.warn("PROBLEME DE CONNEXION A LA BD" + e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.debug("PROBLEME DE FERMETURE DE FICHIER" + e);
				}
			}
		}
	}

	/**
	 * Permet de récupérer une DAO spécifique
	 * 
	 * @param type
	 *            le type de la DAO voulue
	 * @return le DAO voulue
	 * @throws NoDAOException
	 *             DAO non trouvée
	 * @throws NoFactoryException
	 *             Fabrique non créée
	 */
	public static DAO<?> getDAO(DAOType type) throws NoDAOException, NoFactoryException {
		if (factory == null) {
			factory = new DAOFactory();
		}
		if (type == null) {
			throw new NoDAOException("pas de type donné");
		}
		switch (type) {
		case COMPANY:
			return CompanyDAO.getInstance(connection);
		case COMPUTER:
			return ComputerDAO.getInstance(connection);
		default:
			throw new NoDAOException("impossible de trouver une DAO");
		}
	}

	/**
	 * Cloture la connexion
	 */
	protected void finalize() throws Throwable {
		connection.close();
	}
}
