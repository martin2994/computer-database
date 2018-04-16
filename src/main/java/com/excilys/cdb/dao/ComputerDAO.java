package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import main.java.com.excilys.cdb.model.Computer;

public class ComputerDAO implements DAO<Computer> {

	private Connection connection;
	private PreparedStatement statement;

	public ComputerDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Computer> findAll() throws SQLException {
		statement = connection.prepareStatement("SELECT * FROM computer");
		ResultSet rs = statement.executeQuery();
		List<Computer> computers = new ArrayList<>();
		while (rs.next()) {
			// computers.add(new
			// Computer(rs.getInt("id"),rs.getString("name"),rs.getDate("introduced"),rs.getDate("discontinued"),rs.getInt("company_id")));
		}
		return computers;
	}

	@Override
	public Computer findOneById(int id) throws SQLException {
		statement = connection.prepareStatement("SELECT * FROM computer WHERE id=?");
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
				// return new Computer(rs.getInt("id"),rs.getString("name"));
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
