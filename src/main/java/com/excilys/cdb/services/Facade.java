package main.java.com.excilys.cdb.services;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.dao.impl.CompanyDAO;
import main.java.com.excilys.cdb.dao.impl.ComputerDAO;
import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.utils.Page;

/**
 * Facade Regroupe les différentes actions de l'utilisateur
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
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Facade.class);

	/**
	 * le singleton
	 */
	private static Facade facade;
	
	/**
	 * Constructeur qui récupère les différentes DAO
	 */
	private Facade() {
		try {
			this.companyDAO = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
			this.computerDAO = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
		} catch (NoDAOException | NoFactoryException e) {
			logger.debug("DAO exception: " + e.getMessage());
		}
	}

	/**
	 * Récupère la liste des computer
	 * 
	 * @return La liste des computer
	 */
	public Page<Computer> getComputers(int page) {
		try {
			if(page >= 0) {
				return computerDAO.findAll(page);
			}else {
				logger.info("INVALID COMPUTER PAGE");
			}
		} catch (SQLException e) {
			logger.debug("FIND ALL COMPUTERS: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Récupère la liste des company
	 * 
	 * @return La liste des company
	 */
	public Page<Company> getCompanies(int page) {
		try {
			if(page>=0) {
				return companyDAO.findAll(page);
			}else {
				logger.info("INVALID COMPANY PAGE");
			}
		} catch (SQLException e) {
			logger.debug("FIND ALL COMPANIES: " + e.getMessage());
		}
		return null;
	}

	public Company getCompany(int id) {
		try {
			if(id > 0) {
				return companyDAO.findById(id);
			}else {
				logger.info("INVALID COMPANY ID FOR DETAILS");
			}
		} catch (SQLException e) {
			logger.debug("GET COMPANY " + id + ": " + e.getMessage());
		}
		return null;
	}

	/**
	 * Récupère un computer spécifique
	 * 
	 * @param id
	 *            l'id du computer à chercher
	 */
	public Computer getComputerDetails(int id) {
		try {
			if(id > 0) {
				return computerDAO.findById(id);
			}else {
				logger.info("INVALID COMPUTER ID FOR DETAILS");
			}
		} catch (SQLException e) {
			logger.debug("GET COMPUTER " + id + ": " + e.getMessage());
		}
		return null;
	}

	/**
	 * Crée un computer
	 * 
	 * @param computer
	 *            le computer à créer
	 */
	public int createComputer(Computer computer) {
		try {
			if (computer.getIntroduced().isAfter(computer.getDiscontinued()) && computer != null) {
				return computerDAO.add(computer);
			} else {
				logger.info("INVALID COMPUTER FOR CREATE");
			}
		} catch (SQLException e) {
			logger.debug("CREATE COMPUTER " + computer.getId() + ": " + e.getMessage());
		}
		return 0;
	}

	/**
	 * Modifie un computer
	 * 
	 * @param computer
	 *            les nouvelles informations du computer à modifier
	 */
	public Computer updateComputer(Computer computer) {
		try {
			if (computer.getIntroduced().isAfter(computer.getDiscontinued()) && computer != null) {
				return computerDAO.update(computer);
			} else {
				logger.info("INVALID COMPUTER FOR UPDATE");
			}
		} catch (SQLException e) {
			logger.debug("UPDATE COMPUTER " + computer.getId() + ": " + e.getMessage());
		}
		return null;
	}

	/**
	 * Supprime un computer
	 * 
	 * @param id
	 *            l'id du computer à supprimer
	 */
	public void deleteCompute(int id) {
		try {
			if( id > 0) {
				computerDAO.delete(id);
			}else {
				logger.info("INVALID COMPUTER ID FOR DELETE");
			}
		} catch (SQLException e) {
			logger.debug("DELETE COMPUTER " + id + ": " + e.getMessage());
		}
	}

	
	public static Facade getInstance() {
		if( facade == null) {
			facade = new Facade();
		}
		return facade;
	}

}
