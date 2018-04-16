package main.java.com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.List;


import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.ComputerDAO;
import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.exceptions.NoDAOException;
import main.java.com.excilys.cdb.exceptions.NoFactoryException;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public class FacadeImpl implements Facade {

	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	
	public FacadeImpl() {
		try {
			this.companyDAO = (CompanyDAO) DAOFactory.getDAO("Company");
			this.computerDAO = (ComputerDAO) DAOFactory.getDAO("Computer");
		} catch (NoDAOException | NoFactoryException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Computer> getComputers() {
		try {
			return computerDAO.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Company> getCompanies() {
		try {
			return companyDAO.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Computer getComputerDetails(int id) {
		try {
			return computerDAO.findOneById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void createComputer(Computer computer) {
		try {
			computerDAO.add(computer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateComputer(Computer computer) {
		computerDAO.update(computer);
	}

	@Override
	public void deleteCompute(int id) {
		computerDAO.delete(id);
	}

}
