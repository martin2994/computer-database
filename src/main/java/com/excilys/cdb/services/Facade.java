package main.java.com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.List;


import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

/**
 * Facade
 * Regroupe les différentes actions de l'utilisateur
 */
public class Facade {

	/**
	 * DAO de company
	 */
	private CompanyDAO companyDAO;
	
	/**
	 * DAO de computer
	 */
	private ComputerDAO computerDAO;
	
	/**
	 * Constructeur qui récupère les différentes DAO
	 */
	public Facade() {
		try {
			this.companyDAO = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
			this.computerDAO = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
		} catch (NoDAOException | NoFactoryException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Récupère la liste des computer
	 * @return La liste des computer
	 */
	public List<Computer> getComputers() {
		try {
			return computerDAO.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Récupère la liste des company
	 * @return La liste des company
	 */
	public List<Company> getCompanies() {
		try {
			return companyDAO.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Company getCompany(int id) {
		try {
			return companyDAO.findById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Récupère un computer spécifique
	 * @param id l'id du computer à chercher
	 */
	public Computer getComputerDetails(int id) {
		try {
			return computerDAO.findById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Crée un computer
	 * @param computer le computer à créer
	 */
	public void createComputer(Computer computer) {
		try {
			computerDAO.add(computer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifie un computer
	 * @param computer les nouvelles informations du computer à modifier
	 */
	public void updateComputer(Computer computer) {
		try {
			computerDAO.update(computer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Supprime un computer
	 * @param id l'id du computer à supprimer
	 */
	public void deleteCompute(int id) {
		try {
			computerDAO.delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
