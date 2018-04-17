package main.java.com.excilys.cdb.controller;

import java.util.List;


import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.services.Facade;

public class CDBController {

	private Facade facade;
	
	public CDBController(Facade facade) {
		this.facade = facade;
	}

	public List<Computer> getComputers() {
		return facade.getComputers();
	}

	public List<Company> getCompanies() {
		return facade.getCompanies();
	}
	
	public void deleteCompute(int parseInt) {
		facade.deleteCompute(parseInt);
	}

	public void updateComputer(Computer computerUpdate) {
		facade.updateComputer(computerUpdate);
	}

	public Company getCompany(int parseInt) {
		return facade.getCompany(parseInt);
	}

	public Computer getComputerDetails(int parseInt) {
		return facade.getComputerDetails(parseInt);
	}

	public void createComputer(Computer computer) {
		facade.createComputer(computer);
		
	}


	
}
