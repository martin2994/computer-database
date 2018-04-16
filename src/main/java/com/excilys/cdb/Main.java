package main.java.com.excilys.cdb;

import java.util.List;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.services.Facade;
import main.java.com.excilys.cdb.services.FacadeImpl;

public class Main {
	public static void main(String[] args) {
			Facade maFacade = new FacadeImpl();
			List<Company> companies = maFacade.getCompanies();
			System.out.println("COMPANIES:");
			for(Company company: companies) {
				System.out.println(company.getName());
			}

	}

}
