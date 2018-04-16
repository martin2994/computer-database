package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;

public class DAOFactory {

	private static Connection connection;
	private static DAOFactory factory;
	
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
	
	public static DAO<?> getDAO(String type) throws NoDAOException, NoFactoryException{
		if (factory == null) {
			factory = new DAOFactory();
		}
		if(factory == null) {
			throw new NoFactoryException("pas de factory");
		}
		switch (type) {
		case "Company":
			return new CompanyDAO(connection); 
		case "Computer":
			return new ComputerDAO(connection);
		default:
			throw new NoDAOException("impossible de trouver une DAO");
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		connection.close();
	}
}
