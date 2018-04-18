package main.java.com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

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
	
	
	private static final Logger logger = LoggerFactory.getLogger(Facade.class);
	
	/**
	 * Constructeur qui récupère les différentes DAO
	 */
	public Facade() {
		try {
			this.companyDAO = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
			this.computerDAO = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
		} catch (NoDAOException | NoFactoryException e) {
			logger.debug("DAO exception: "+ e);
		}
	}
	
	/**
	 * Récupère la liste des computer
	 * @return La liste des computer
	 */
	public Page<Computer> getComputers(int page) {
		try {
			return computerDAO.findAll(page);
		} catch (SQLException e) {
			logger.debug("FIND ALL COMPUTERS: "+ e);
		}
		return null;
	}

	/**
	 * Récupère la liste des company
	 * @return La liste des company
	 */
	public Page<Company> getCompanies(int page) {
		try {
			return companyDAO.findAll(page);
		} catch (SQLException e) {
			logger.debug("FIND ALL COMPANIES: "+ e);
		}
		return null;
	}

	public Company getCompany(int id) {
		try {
			return companyDAO.findById(id);
		} catch (SQLException e) {
			logger.debug("GET COMPANY "+ id +": "+ e);
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
			logger.debug("GET COMPUTER "+ id +": "+ e);
		}
		return null;
	}

	/**
	 * Crée un computer
	 * @param computer le computer à créer
	 */
	public void createComputer(Computer computer) {
		try {
			if(computer != null) {
				computerDAO.add(computer);
			}else {
				logger.info("COMPUTER NULL");
			}
		} catch (SQLException e) {
			logger.debug("CREATE COMPUTER "+ computer.getId() +": "+ e);
		}
	}

	/**
	 * Modifie un computer
	 * @param computer les nouvelles informations du computer à modifier
	 */
	public void updateComputer(Computer computer) {
		try {
			if(computer != null) {
				computerDAO.update(computer);
			}else {
				logger.info("COMPUTER NULL");
			}
		} catch (SQLException e) {
			logger.debug("UPDATE COMPUTER "+ computer.getId() +": "+ e);
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
			logger.debug("DELETE COMPUTER "+ id +": "+ e);
		}
	}

}
