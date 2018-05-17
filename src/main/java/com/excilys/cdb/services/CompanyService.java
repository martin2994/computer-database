package com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;
import com.excilys.cdb.validators.CompanyValidator;

@Service
public class CompanyService {
    /**
     * DAO de company.
     */
    @Autowired
    private CompanyDAO companyDAO;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    /**
     * Récupère la liste de toutes les company.
     * @return la liste des company
     */
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        try {
            companies = companyDAO.findAll();
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPUTERS: " + e.getMessage());
        }
        return companies;
    }

    /**
     * Récupère la liste des company par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            le nombre de computer par page
     * @return La liste des company
     */
    public Page<Company> getCompanies(int page, int resultPerPage) {
        Page<Company> cPage = new Page<>();
        try {
            if (page >= 0 && resultPerPage >= 1) {
                cPage = companyDAO.findPerPage(page, resultPerPage);
            } else {
                LOGGER.info("INVALID COMPANY PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPANIES: " + e.getMessage());
        }
        return cPage;
    }

    /**
     * Récupère une company en fonction de son id.
     * @param id
     *            l'id de la company
     * @return la company
     * @throws InvalidCompanyException
     *             Exception sur les companies
     */
    public Company getCompany(long id) throws InvalidCompanyException {
        try {
            CompanyValidator.isValidId(id);
            return companyDAO.findById(id).orElseThrow(() -> new InvalidCompanyException("L'id n'est pas valide."));
        } catch (SQLException e) {
            LOGGER.debug("GET COMPANY " + id + ": " + e.getMessage());
        }
        throw new InvalidCompanyException("Une erreur est survenue.");
    }

    /**
     * Permet de supprimer une company.
     * @param id
     *            l'id de la company à supprimer
     * @return un boolean pour savoir si la suppression a eu lieu
     * @throws InvalidCompanyException
     *             Exception lancée si l'id n'est pas valide
     */
    public boolean deleteCompany(long id) throws InvalidCompanyException {
        boolean result = false;
        try {
            CompanyValidator.isValidId(id);
            if (companyDAO.delete(id)) {
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.debug("DELETE COMPUTER " + id + ": " + e.getMessage());
        }
        return result;
    }

}
