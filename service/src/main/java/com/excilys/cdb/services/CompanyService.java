package com.excilys.cdb.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;
import com.excilys.cdb.validators.CompanyValidator;

@Service
public class CompanyService {
	/**
	 * DAO de company.
	 */
	private CompanyDAO companyDAO;

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

	/**
	 * Constructeur privé et injecte la dao.
	 * 
	 * @param companyDAO
	 *            la dao des companies
	 */
	private CompanyService(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * Récupère la liste de toutes les companies.
	 * 
	 * @return la liste des company
	 */
	public List<Company> getCompanies() {
		List<Company> companies = new ArrayList<>();
		companies = companyDAO.findAll();
		return companies;
	}

	/**
	 * Récupère la liste des companies par page.
	 * 
	 * @param page
	 *            la page à afficher
	 * @param resultPerPage
	 *            le nombre de computers par page
	 * @return La liste des company
	 * @throws InvalidCompanyException
	 *             Exception lancée quand la requete echoue
	 */
	public Page<Company> getCompanies(int page, int resultPerPage) throws InvalidCompanyException {
		Page<Company> cPage = new Page<>();
		if (page >= 0 && resultPerPage >= 1) {
			cPage = companyDAO.findPerPage(page, resultPerPage);
		} else {
			LOGGER.info("INVALID COMPANY PAGE");
		}
		return cPage;
	}

	/**
	 * Récupère la liste des companies par page.
	 * 
	 * Récupère la liste de toutes les computers par un id de company.
	 * 
	 * @return la liste des computers
	 */
	public List<Computer> getComputersByCompanyId(long id) {
		List<Computer> computers = new ArrayList<>();
		computers = companyDAO.getComputerByCompanyId(id);
		return computers;
	}

	/**
	 * Récupère la liste des computers en fonction de id company par page.
	 * 
	 * @param id
	 *            id de company
	 * @param page
	 *            la page à afficher
	 * @param resultPerPage
	 *            le nombre de computers par page
	 * @return La liste des company
	 * @throws Exception
	 */
	public Page<Company> getCompaniesByName(int page, int resultPerPage, String search) throws Exception {
		Page<Company> cPage = new Page<>();
		if (page >= 0 && resultPerPage >= 1) {
			cPage = companyDAO.findPerPageByName(page, resultPerPage, search);
		} else {
			LOGGER.info("INVALID COMPANY PAGE");
		}
		return cPage;
	}

	/**
	 * @throws InvalidComputerException
	 *             Exception lancée quand la requete echoue
	 */
	public Page<Computer> getComputersByCompanyId(long id, int page, int resultPerPage)
			throws InvalidComputerException {
		Page<Computer> cPage = new Page<>();
		if (page >= 0 && resultPerPage >= 1) {
			cPage = companyDAO.getComputerByCompanyIdPerPage(id, page, resultPerPage);
		} else {
			LOGGER.info("INVALID COMPUTER PAGE");
		}
		return cPage;
	}

	/**
	 * Récupère une company en fonction de son id.
	 * 
	 * @param id
	 *            l'id de la company
	 * @return la company
	 * @throws InvalidCompanyException
	 *             Exception sur les companies
	 * @throws NoObjectException
	 *             Exception lancée quand la requete echoue (pas de resultat)
	 * @throws InvalidIdException
	 */
	public Company getCompany(long id) throws NoObjectException, InvalidIdException, InvalidCompanyException {
		CompanyValidator.isValidId(id);
		return companyDAO.findById(id)
				.orElseThrow(() -> new InvalidIdException(ExceptionMessage.INVALID_ID.getMessage()));
	}

	/**
	 * Permet d'ajouter une nouvelle company.
	 * 
	 * @param company
	 *            les infos de la company
	 * @return l'id de la nouvelle company
	 * @throws InvalidCompanyException
	 *             Exception lancée quand les infos sont invalides
	 * @throws NoObjectException
	 *             Exception lancé quand il n'y a pas de résultat
	 */
	public long createCompany(Company company) throws InvalidCompanyException, NoObjectException {
		CompanyValidator.isValidCompany(company);
		return companyDAO.add(company);
	}

	/**
	 * Permet de modifier une company.
	 * 
	 * @param company
	 *            les nouvelles infos de la company
	 * @return la nouvelle company
	 * @throws InvalidCompanyException
	 *             Exception lancée quand les infos sont invalides
	 * @throws NoObjectException
	 *             Exception lancé quand il n'y a pas de résultat
	 * @throws InvalidIdException
	 *             Exception lancée quand l'id est invalide
	 */
	public Company updateCompany(Company company)
			throws InvalidCompanyException, NoObjectException, InvalidIdException {
		CompanyValidator.isValidCompany(company);
		if (!companyDAO.isExist(company.getId())) {
			LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
			throw new InvalidIdException(ExceptionMessage.NO_COMPANY.getMessage());
		}
		return companyDAO.update(company)
				.orElseThrow(() -> new InvalidCompanyException(ExceptionMessage.INVALID_INFO.getMessage()));
	}

	/**
	 * Permet de supprimer une company.
	 * 
	 * @param id
	 *            l'id de la company à supprimer
	 * @return un boolean pour savoir si la suppression a eu lieu
	 * @throws InvalidCompanyException
	 *             Exception lancée si l'id n'est pas valide
	 */
	public boolean deleteCompany(long id) throws InvalidCompanyException {
		boolean result = false;
		CompanyValidator.isValidId(id);
		if (companyDAO.delete(id)) {
			result = true;
		}
		return result;
	}

	/**
	 * Permet d'avoir le nombre de companies.
	 * 
	 * @return le nombre de companies
	 */
	public int getCountCompanies() {
		return companyDAO.count();
	}

	public Page<Computer> findComputersByCompanyId(Long id, int page, int resultPerPage, String search) 
			throws InvalidComputerException {
		Page<Computer> cPage = new Page<>();
		if (page >= 0 && resultPerPage >= 1) {
			cPage = companyDAO.findComputerByCompanyIdPerPage(id, page, resultPerPage, search);
		} else {
			LOGGER.info("INVALID COMPUTER PAGE");
		}
		return cPage;
	}

}
