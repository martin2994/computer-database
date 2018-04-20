package main.java.com.excilys.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import main.java.com.excilys.cdb.dao.DAO;
import main.java.com.excilys.cdb.mapper.DateMapper;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

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
	private final String ALL_COMPUTERS = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id LIMIT ?,?";
	
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
	 * Requete pour le nombre de page
	 */
	private final String MAX_PAGE = "SELECT COUNT(id) FROM computer";
	
	/**
	 * le singleton
	 */
	private static ComputerDAO computerDAO;
	
	/**
	 * Constructeur pour initialiser la connexion
	 * @param connection La connexion en cours
	 */
	private ComputerDAO(Connection connection) {
		this.connection = connection;
	}

	
	/**
	 * Permet de récupérer la liste de tous les computer
	 * @return La liste des computer
	 */
	@Override
	public Page<Computer> findAll(int page) throws SQLException {
		Page<Computer> computers = new Page<>();
		statement = connection.prepareStatement(ALL_COMPUTERS,ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1,page*Page.resultsPerPage);
		statement.setInt(2,Page.resultsPerPage);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			Company company;
			if(rs.getInt("company.id") > 0){
				company = new Company(rs.getInt("company.id"),rs.getString("company.name"));
			}else {
				company = null;
			}
			Computer computer = new Computer.Builder(rs.getString("computer.name"))
					.id(rs.getInt("computer.id"))
					.introduced(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.introduced")))
					.discontinued(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.discontinued")))
					.manufacturer(company).build();
			 
			 
			computers.add(computer);
		}
		rs.close();
		statement.close();
		computers.setCurrentPage(page);
		computers.setMaxPage(getMaxPage());
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
			computer = new Computer.Builder(rs.getString("computer.name"))
						.id(rs.getInt("computer.id"))
						.introduced(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.introduced")))
						.discontinued(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.discontinued")))
						.manufacturer(company).build();
				 
		}
		return computer;
	}

	/**
	 * Ajoute un computer dans la base
	 * @param computer, les informations du computer à ajouter
	 */
	@Override
	public int add(Computer computer) throws SQLException {
		statement = connection.prepareStatement(INSERT_COMPUTER,Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, computer.getName());
		Timestamp date = null;
		if(computer.getIntroduced() != null) {
			date =  DateMapper.convertLocalDateToTimeStamp(computer.getIntroduced());
		}
		statement.setTimestamp(2,date);
		if(computer.getDiscontinued() != null) {
			date =  DateMapper.convertLocalDateToTimeStamp(computer.getDiscontinued());
		}
		statement.setTimestamp(3,date);
		if(computer.getManufacturer() != null) {
			statement.setLong(4, computer.getManufacturer().getId());
		}else {
			statement.setObject(4, null);
		}
		statement.executeUpdate();
		ResultSet resultSet = statement.getGeneratedKeys();
		if(resultSet.next()) {
			return resultSet.getInt(1);
		}
		return 0;
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
	public Computer update(Computer computer) throws SQLException {
		statement = connection.prepareStatement(UPDATE_COMPUTER);
		statement.setString(1, computer.getName());
		Timestamp date = null;
		if(computer.getIntroduced() != null) {
			date = DateMapper.convertLocalDateToTimeStamp(computer.getIntroduced());
		}
		statement.setTimestamp(2,date);
		if(computer.getDiscontinued() != null) {
			date =  DateMapper.convertLocalDateToTimeStamp(computer.getDiscontinued());
		}
		statement.setTimestamp(3,date);
		if(computer.getManufacturer() != null) {
			statement.setLong(4, computer.getManufacturer().getId());
		}else {
			statement.setObject(4,null);
		}
		statement.setLong(5, computer.getId());
		int result = statement.executeUpdate();
		if( result == 1) {
			return computer;
		}
		return null;
	}
	
	/**
	 * Récupère le nombre de page total
	 */
	@Override
	public int getMaxPage() throws SQLException {
		statement = connection.prepareStatement(MAX_PAGE);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			return rs.getInt(1)/Page.resultsPerPage;
		}
		return 0;
	}
	
	/**
	 * Récupère le singleton computerDAO
	 * @param connection la connexion à la BD
	 * @return le ComputerDAO
	 */
	public static ComputerDAO getInstance(Connection connection) {
		if(computerDAO == null) {
			computerDAO = new ComputerDAO(connection);
		}
		return computerDAO;
	}
	

}
