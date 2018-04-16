package main.java.com.excilys.cdb.services;

import java.util.List;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public interface Facade {
	List<Computer> getComputers();
	List<Company> getCompanies();
	Computer getComputerDetails(int id);
	void createComputer(Computer computer);
	void updateComputer(Computer computer);
	void deleteCompute(int id);
	
}
