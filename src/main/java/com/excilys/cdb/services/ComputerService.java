package com.excilys.cdb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.enums.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;
import com.excilys.cdb.validators.ComputerValidator;

@Service
public class ComputerService {
    /**
     * DAO de company.
     */
    private CompanyDAO companyDAO;

    /**
     * DAO de computer.
     */
    private ComputerDAO computerDAO;

    private MessageSource messageSource;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);

    /**
     * Constructeur privé et injecte les DAO.
     * @param companyDAO la dao des companies
     * @param computerDAO la dao des computers
     * @param messageSource
     *            Message Internationaliser
     */
    @Autowired
    private ComputerService(CompanyDAO companyDAO, ComputerDAO computerDAO, MessageSource messageSource) {
        this.companyDAO = companyDAO;
        this.computerDAO = computerDAO;
        this.messageSource = messageSource;
    }

    /**
     * Récupère la liste des computers par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            le nombre de computers par page
     * @return La liste des computer
     * @throws InvalidComputerException
     *             Exception lancée quand une requete a echoué
     */
    public Page<Computer> getComputers(int page, int resultPerPage) throws InvalidComputerException {
        Page<Computer> cPage = new Page<>();
        if (page >= 0 && resultPerPage >= 1) {
            cPage = computerDAO.findPerPage(page, resultPerPage);
        } else {
            LOGGER.info("INVALID COMPUTER PAGE");
        }

        return cPage;
    }

    /**
     * Récupère la liste des computers triée et recherchée par page.
     * @param search
     *            le nom à rechercher
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            le nombre de computer par page
     * @return La liste des computer
     * @throws InvalidComputerException
     *             Exception lancée quand une requete a échoué
     */
    public Page<Computer> getComputersByName(String search, int page, int resultPerPage)
            throws InvalidComputerException {
        Page<Computer> cPage = new Page<>();
        if (page >= 0 && resultPerPage >= 1) {
            cPage = computerDAO.findByNamePerPage(search, page, resultPerPage);
        } else {
            LOGGER.info("INVALID COMPUTER PAGE");
        }
        return cPage;
    }

    /**
     * Récupère un computer spécifique.
     * @param id
     *            l'id du computer à chercher
     * @return le computer
     * @throws InvalidComputerException
     *             Exception sur les computers
     * @throws NoObjectException
     *             Exception lancée quand la requete echoue ( pas de resultat)
     */
    public Computer getComputerDetails(long id) throws InvalidComputerException, NoObjectException {
        ComputerValidator.isValidId(id);
        return computerDAO.findById(id).orElseThrow(() -> new InvalidComputerException(messageSource.getMessage(ExceptionMessage.INVALID_ID.getMessage(), null, LocaleContextHolder.getLocale())));
    }

    /**
     * Crée un computer.
     * @param computer
     *            le computer à créer
     * @return l'id du nouveau computer
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     */
    public long createComputer(Computer computer) throws InvalidComputerException, InvalidCompanyException {
        long result = 0;
        try {
            ComputerValidator.isValidComputer(computer);
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
                    throw new InvalidCompanyException(messageSource.getMessage(ExceptionMessage.NO_COMPANY.getMessage(), null, LocaleContextHolder.getLocale()));
                }
            }
            result = computerDAO.add(computer);
        } catch (NoObjectException e) {
            LOGGER.debug("CREATE COMPUTER WITH NULL OBJECT" + e.getMessage());
            throw new InvalidComputerException(messageSource.getMessage(ExceptionMessage.UNCOMPLETE_INFO.getMessage(), null, LocaleContextHolder.getLocale()));
        }
        return result;
    }

    /**
     * Modifie un computer.
     * @param computer
     *            les nouvelles informations du computer à modifier
     * @return le nouveau computer
     * @throws InvalidComputerException
     *             Exception sur les computers
     * @throws InvalidCompanyException
     *             Exception sur les companies
     */
    public Computer updateComputer(Computer computer) throws InvalidComputerException, InvalidCompanyException {
        try {
            ComputerValidator.isValidComputer(computer);
            if (!computerDAO.isExist(computer.getId())) {
                LOGGER.info("INVALID COMPUTER FOR UPDATE");
                throw new InvalidComputerException(messageSource.getMessage(ExceptionMessage.NO_COMPUTER.getMessage(), null, LocaleContextHolder.getLocale()));
            }
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
                    throw new InvalidCompanyException(messageSource.getMessage(ExceptionMessage.NO_COMPANY.getMessage(), null, LocaleContextHolder.getLocale()));
                }
            }
            return computerDAO.update(computer)
                    .orElseThrow(() -> new InvalidComputerException(messageSource.getMessage(ExceptionMessage.INVALID_INFO.getMessage(), null, LocaleContextHolder.getLocale())));
        } catch (NoObjectException e) {
            LOGGER.debug("UPDATE COMPUTER NULL " + e.getMessage());
        }
        throw new InvalidComputerException(messageSource.getMessage(ExceptionMessage.ERROR.getMessage(), null, LocaleContextHolder.getLocale()));
    }

    /**
     * Supprime un computer.
     * @param id
     *            l'id du computer à supprimer
     * @return Si la suppression a été effectuée
     * @throws InvalidIdException
     *             Exception lancée si l'id du computer n'est pas valide
     */
    public boolean deleteComputer(long id) throws InvalidIdException {
        boolean result = false;
        ComputerValidator.isValidId(id);
        if (computerDAO.delete(id)) {
            result = true;
        }
        return result;
    }

    /**
     * Permet de supprimer une liste de computers.
     * @param idList
     *            la liste d'id
     * @return un boolean de réussite ou non
     */
    public boolean deleteComputerList(String idList) {
        boolean result = false;
        if (computerDAO.deleteList(idList)) {
            result = true;
        }
        return result;
    }

    /**
     * Permet d'avoir le nombre de computers.
     * @return le nombre de computers
     */
    public int getCountComputers() {
        int result = 0;
        result = computerDAO.count();
        return result;
    }

    /**
     * Permet d'avoir le nombre de computers d'une recherche.
     * @param search
     *            le nom à rechercher
     * @return le nombre de computers
     */
    public int getCountComputersByName(String search) {
        int result = 0;
        result = computerDAO.countByName(search);
        return result;
    }

}
