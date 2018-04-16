package main.java.com.excilys.cdb;

import java.util.List;

import main.java.com.excilys.cdb.dao.CompanyDAO;
import main.java.com.excilys.cdb.dao.CompanyDAOImpl;
import main.java.com.excilys.cdb.model.Company;

public class Main {
	public static void main(String[] args) {
		CompanyDAO companyDAO = new CompanyDAOImpl();
		List<Company> companies = companyDAO.findAll();
		System.out.println("COMPANIES:");
		for(Company company: companies) {
			System.out.println(company.getName());
		}
		Company company = companyDAO.findOneById(2);
		System.out.println("COMPANY 2:");
		System.out.println(company.getName());
	}

}
