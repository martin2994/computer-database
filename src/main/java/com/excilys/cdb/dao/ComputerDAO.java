package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sun.jmx.snmp.Timestamp;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public class ComputerDAO implements DAO<Computer> {

	private Connection connection;
	private PreparedStatement statement;
	private final String ALL_COMPUTERS = "SELECT * FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";
	private final String COMPUTER_BY_ID = "SELECT * FROM computer WHERE computer.id=? LEFT OUTER JOIN company ON computer.company_id=company.id";
	public ComputerDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Computer> findAll() throws SQLException {
		statement = connection.prepareStatement(ALL_COMPUTERS);
		ResultSet rs = statement.executeQuery();
		List<Computer> computers = new ArrayList<>();
		while (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			Computer computer = new Computer(rs.getInt("computer.id"),rs.getString("computer.name"),rs.getDate("computer.introduced"),rs.getDate("computer.discontinued"),company);
			 
			computers.add(computer);
		}
		return computers;
	}

	
	@Override
	public Computer findOneById(int id) throws SQLException {
		statement = connection.prepareStatement(COMPUTER_BY_ID);
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		Computer computer = null;
		if (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computer = new Computer(rs.getInt("computer.id"),rs.getString("computer.name"),rs.getDate("computer.introduced"),rs.getDate("computer.discontinued"),company);
		}
		return computer;
	}

	@Override
	public void add(Computer computer) throws SQLException {
		statement = connection.prepareStatement("INSERT INTO computer (id,name,introduced,discontinued,company_id) values (?,?,?,?,?)");
		statement.setInt(1, computer.getId());
		statement.setString(2, computer.getName());
		statement.setDate(3, new java.sql.Date(computer.getIntroduced().getTime()));
		statement.setDate(4, new java.sql.Date(computer.getDiscontinued().getTime()));
		statement.setInt(5, computer.getManufacturer().getId());
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
