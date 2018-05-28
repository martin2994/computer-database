package com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.enums.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
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

    private MessageSource messageSource;

    /**
     * Constructeur privé et injecte la dao.
     * @param companyDAO la dao des companies
     * @param messageSource
     *            Message Internationaliser
     */
    @Autowired
    private CompanyService(CompanyDAO companyDAO, MessageSource messageSource) {
        this.companyDAO = companyDAO;
        this.messageSource = messageSource;
    }

    /**
     * Récupère la liste de toutes les companies.
     * @return la liste des company
     */
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        companies = companyDAO.findAll();
        return companies;
    }

    /**
     * Récupère la liste des companies par page.
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
     * Récupère une company en fonction de son id.
     * @param id
     *            l'id de la company
     * @return la company
     * @throws InvalidCompanyException
     *             Exception sur les companies
     * @throws NoObjectException
     *             Exception lancée quand la requete echoue (pas de resultat)
     */
    public Company getCompany(long id) throws InvalidCompanyException, NoObjectException {
        CompanyValidator.isValidId(id);
        return companyDAO.findById(id).orElseThrow(() -> new InvalidCompanyException(messageSource.getMessage(ExceptionMessage.INVALID_ID.getMessage(), null, LocaleContextHolder.getLocale())));
    }

    /**
     * Permet de supprimer une company.
     * @param id
     *            l'id de la company à supprimer
     * @return un boolean pour savoir si la suppression a eu lieu
     * @throws InvalidCompanyException
     *             Exception lancée si l'id n'est pas valide
     */
    @Transactional(readOnly = false, rollbackFor = SQLException.class)
    public boolean deleteCompany(long id) throws InvalidCompanyException {
        boolean result = false;
        CompanyValidator.isValidId(id);
        if (companyDAO.delete(id)) {
            result = true;
        }
        return result;
    }

}
