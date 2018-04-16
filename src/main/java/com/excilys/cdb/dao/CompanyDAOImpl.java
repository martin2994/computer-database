package main.java.com.excilys.cdb.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import main.java.com.excilys.cdb.model.Company;

public class CompanyDAOImpl implements CompanyDAO {

	private Connection connection;
	private Statement statement;
	
	public CompanyDAOImpl() {
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
	public List<Company> findAll(){
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM company");
			List<Company> companies = new ArrayList<>();
		    while (rs.next()) {
		       companies.add(new Company(rs.getInt("id"),rs.getString("name")));
		    }
			return companies;
		}catch (Exception e) {
			e.printStackTrace();		
		}
		return null;
	}

	@Override
	public Company findOneById(int id) {
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM company WHERE id="+id);
			if(rs.next()) {
				return new Company(rs.getInt("id"),rs.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();		
		}
		return null;
	}

}
