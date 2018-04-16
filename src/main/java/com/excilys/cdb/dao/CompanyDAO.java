package main.java.com.excilys.cdb.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import main.java.com.excilys.cdb.model.Company;

public class CompanyDAO implements DAO<Company> {

	private Connection connection;
	private PreparedStatement statement;
	
	public CompanyDAO(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public List<Company> findAll() throws SQLException{
		statement = connection.prepareStatement("SELECT * FROM company");
		ResultSet rs = statement.executeQuery();
		List<Company> companies = new ArrayList<>();
	    while (rs.next()) {
	       companies.add(new Company(rs.getInt("id"),rs.getString("name")));
	    }
		return companies;
	}

	@Override
	public Company findOneById(int id) throws SQLException {
		statement = connection.prepareStatement("SELECT * FROM company WHERE id=?");
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			return new Company(rs.getInt("id"),rs.getString("name"));
		}
		return null;
	}

	@Override
	public void add(Company t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Company t) {
		// TODO Auto-generated method stub
		
	}

}
