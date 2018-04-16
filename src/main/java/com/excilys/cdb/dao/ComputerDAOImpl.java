package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.java.com.excilys.cdb.model.Computer;

public class ComputerDAOImpl implements ComputerDAO {

	private Connection connection;
	private Statement statement;
	
	public ComputerDAOImpl() {
		try {
			connection = DriverManager.getConnection(
	                  "jdbc:mysql://localhost:3306/computer-database-db",
	                  "admincdb",
	                  "qwerty1234");
	
			statement = connection.createStatement();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Computer> findAll() {
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM computer");
			List<Computer> computers = new ArrayList<>();
		    while (rs.next()) {
		    	//computers.add(new Computer(rs.getInt("id"),rs.getString("name"),rs.getDate("")));
		    }
			return computers;
		}catch (Exception e) {
			e.printStackTrace();		
		}
		return null;
	}

	@Override
	public Computer findOneById(int id) {
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM computer WHERE id="+id);
			if(rs.next()) {
			//	return new Computer(rs.getInt("id"),rs.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();		
		}
		return null;
	}

	@Override
	public void add(Computer computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Computer computer) {
		// TODO Auto-generated method stub

	}

}
