package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

/**
 * DAO des Computer
 * Regroupe l'ensemble des transactions sur les computer
 */
public class ComputerDAO implements DAO<Computer> {

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
	private final String ALL_COMPUTERS = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";
	
	/**
	 * Requete pour le findById
	 */
	private final String COMPUTER_BY_ID = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id=company.id  WHERE computer.id=?";
	
	/**
	 * Requete pour l'update
	 */
	private final String UPDATE_COMPUTER = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";
	
	/**
	 * Requete pour le delete
	 */
	private final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";
	
	/**
	 * Requete pour l'insert
	 */
	private final String INSERT_COMPUTER = "INSERT INTO computer (name,introduced,discontinued,company_id) values (?,?,?,?)";
	
	/**
	 * Constructeur pour initialiser la connexion
	 * @param connection La connexion en cours
	 */
	public ComputerDAO(Connection connection) {
		this.connection = connection;
	}

	
	/**
	 * Permet de récupérer la liste de tous les computer
	 * @return La liste des computer
	 */
	@Override
	public List<Computer> findAll() throws SQLException {
		statement = connection.prepareStatement(ALL_COMPUTERS,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery();
		List<Computer> computers = new ArrayList<>();
		while (rs.next()) {
			Company company;
			if(rs.getInt("company.id") > 0){
				company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			}else {
				company = null;
			}
			Computer computer = new Computer(rs.getInt("computer.id"),rs.getString("computer.name"),rs.getDate("computer.introduced"),rs.getDate("computer.discontinued"),company);
			 
			computers.add(computer);
		}
		return computers;
	}

	/**
	 * Récupère un computer particulier
	 * @param id l'id du computer à rechercher
	 * @return Le computer correspondant
	 */
	@Override
	public Computer findById(int id) throws SQLException {
		statement = connection.prepareStatement(COMPUTER_BY_ID,ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		Computer computer = null;
		if (rs.next()) {
			Company company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			computer = new Computer(rs.getInt("computer.id"),rs.getString("computer.name"),rs.getDate("computer.introduced"),rs.getDate("computer.discontinued"),company);
		}
		return computer;
	}

	/**
	 * Ajoute un computer dans la base
	 * @param computer, les informations du computer à ajouter
	 */
	@Override
	public void add(Computer computer) throws SQLException {
		statement = connection.prepareStatement(INSERT_COMPUTER);
		statement.setString(1, computer.getName());
		java.sql.Date intro = null;
		if(computer.getIntroduced() != null) {
			intro =  new java.sql.Date(computer.getIntroduced().getTime());
		}
		statement.setDate(2,intro);
		java.sql.Date disco = null;
		if(computer.getDiscontinued() != null) {
			disco =  new java.sql.Date(computer.getDiscontinued().getTime());
		}
		statement.setDate(3,disco);
		if(computer.getManufacturer() != null) {
			statement.setInt(4, computer.getManufacturer().getId());
		}else {
			statement.setObject(4, null);
		}
		statement.executeUpdate();
	}

	/**
	 * Supprime un computer de la base
	 * @param id, l'id du computer à supprimer
	 */
	@Override
	public void delete(int id) throws SQLException {
		statement = connection.prepareStatement(DELETE_COMPUTER);
		statement.setInt(1, id);
		statement.executeUpdate();
	}

	/**
	 * Modifie un computer avec les informations données
	 * @param computer, les informations du computer à modifier
	 */
	@Override
	public void update(Computer computer) throws SQLException {
		statement = connection.prepareStatement(UPDATE_COMPUTER);
		statement.setString(1, computer.getName());
		java.sql.Date intro = null;
		if(computer.getIntroduced() != null) {
			intro =  new java.sql.Date(computer.getIntroduced().getTime());
		}
		statement.setDate(2,intro);
		java.sql.Date disco = null;
		if(computer.getDiscontinued() != null) {
			intro =  new java.sql.Date(computer.getDiscontinued().getTime());
		}
		statement.setDate(3,disco);
		if(computer.getManufacturer() != null) {
			statement.setInt(4, computer.getManufacturer().getId());
		}else {
			statement.setObject(4,null);
		}
		statement.setInt(5, computer.getId());
		statement.executeUpdate();
	}

}
