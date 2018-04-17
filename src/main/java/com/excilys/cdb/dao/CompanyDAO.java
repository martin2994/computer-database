package main.java.com.excilys.cdb.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import main.java.com.excilys.cdb.model.Company;

/**
 * DAO pour Company
 * Regroupe les différentes transactions sur les Company
 */
public class CompanyDAO implements DAO<Company> {

	/**
	 * Connexion à la BD
	 */
	private Connection connection;
	
	/**
	 * Transaction en cours
	 */
	private PreparedStatement statement;
	
	/**
	 * Requete pour le findAll
	 */
	private final String ALL_COMPANIES = "SELECT id,name FROM company";
	
	/**
	 * Requete pour le findById
	 */
	private final String COMPANY_BY_ID = "SELECT id,name FROM company WHERE id=?";
	
	/**
	 * Constructeur pour initialiser la connexion
	 * @param connection La connexion en cours
	 */
	public CompanyDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Permet de récupérer la liste de toutes les company
	 * @return La liste des Company
	 */
	@Override
	public List<Company> findAll() throws SQLException{
		statement = connection.prepareStatement(ALL_COMPANIES);
		ResultSet rs = statement.executeQuery();
		List<Company> companies = new ArrayList<>();
	    while (rs.next()) {
	       companies.add(new Company(rs.getInt("id"),rs.getString("name")));
	    }
		return companies;
	}

	/**
	 * Récupère une company particulière
	 * @param id , l'id de la company à rechercher
	 * @return La company correspondante
	 */
	@Override
	public Company findById(int id) throws SQLException {
		statement = connection.prepareStatement(COMPANY_BY_ID);
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
