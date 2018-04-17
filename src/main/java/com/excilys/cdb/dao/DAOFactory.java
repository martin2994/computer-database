package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;

/**
 * Fabrique de DAO
 * Singleton retournant les DAO
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
	 * Construction qui créé la connexion entre la BD et l'application
	 */
	private DAOFactory() {
		try {
			connection = DriverManager.getConnection(
	                  "jdbc:mysql://localhost:3306/computer-database-db",
	                  "admincdb",
	                  "qwerty1234");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de récupérer une DAO spécifique
	 * @param type le type de la DAO voulue
	 * @return le DAO voulue
	 * @throws NoDAOException DAO non trouvée
	 * @throws NoFactoryException Fabrique non créée
	 */
	public static DAO<?> getDAO(DAOType type) throws NoDAOException, NoFactoryException{
		if (factory == null) {
			factory = new DAOFactory();
		}
		if(factory == null) {
			throw new NoFactoryException("pas de factory");
		}
		switch (type) {
		case COMPANY:
			return new CompanyDAO(connection); 
		case COMPUTER:
			return new ComputerDAO(connection);
		default:
			throw new NoDAOException("impossible de trouver une DAO");
		}
	}
	
	/**
	 * Cloture la connexion
	 */
	@Override
	protected void finalize() throws Throwable {
		connection.close();
	}
}
