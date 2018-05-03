package com.excilys.cdb.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;
import com.excilys.cdb.validators.CompanyValidator;
import com.excilys.cdb.validators.ComputerValidator;

/**
 * Facade Regroupe les différentes actions de l'utilisateur.
 */
public class Facade {

    /**
     * DAO de company.
     */
    private CompanyDAO companyDAO;

    /**
     * DAO de computer.
     */
    private ComputerDAO computerDAO;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

    /**
     * le singleton.
     */
    private static Facade facade;

    /**
     * Constructeur qui récupère les différentes DAO.
     */
    private Facade() {
        try {
            this.companyDAO = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
            this.computerDAO = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
        } catch (NoDAOException | NoFactoryException e) {
            LOGGER.debug("DAO exception: " + e.getMessage());
        }
    }

    /**
     * Récupère la liste des computer par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            le nombre de computer par page
     * @return La liste des computer
     */
    public Page<Computer> getComputers(int page, int resultPerPage) {
        try {
            if (page >= 0 && resultPerPage >= 1) {
                return computerDAO.findPerPage(page, resultPerPage);
            } else {
                LOGGER.info("INVALID COMPUTER PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPUTERS: " + e.getMessage());
        }
        return new Page<>();
    }

    /**
     * Récupère la liste de toutes les company.
     * @return la liste des company
     */
    public List<Company> getCompanies() {
        try {
            return companyDAO.findAll();
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPUTERS: " + e.getMessage());
        }
        return new ArrayList<>();
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
        try {
            if (page >= 0 && resultPerPage >= 1) {
                return companyDAO.findPerPage(page, resultPerPage);
            } else {
                LOGGER.info("INVALID COMPANY PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPANIES: " + e.getMessage());
        }
        return new Page<>();
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
     * Récupère un computer spécifique.
     * @param id
     *            l'id du computer à chercher
     * @return le computer
     * @throws InvalidComputerException
     *             Exception sur les computers
     */
    public Computer getComputerDetails(long id) throws InvalidComputerException {
        try {
            ComputerValidator.isValidId(id);
            return computerDAO.findById(id).orElseThrow(() -> new InvalidComputerException("L'id n'est pas valide."));
        } catch (SQLException e) {
            LOGGER.debug("GET COMPUTER " + id + ": " + e.getMessage());
        }
        throw new InvalidComputerException("Une erreur est survenue.");
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
        try {
            ComputerValidator.isValidComputer(computer);
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
                    throw new InvalidCompanyException("La company n'existe pas.");
                }
            }
            return computerDAO.add(computer);
        } catch (SQLException e) {
            LOGGER.debug("CREATE COMPUTER " + computer.getId() + ": " + e.getMessage());
        } catch (NoObjectException e) {
            LOGGER.debug("CREATE COMPUTER WITH NULL OBJECT" + e.getMessage());
        }
        return 0;
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
                throw new InvalidComputerException("Le computer n'existe pas.");
            }
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
                    throw new InvalidCompanyException("La company n'existe pas.");
                }
            }
            return computerDAO.update(computer)
                    .orElseThrow(() -> new InvalidComputerException("Les infos du computer ne sont pas valides."));

        } catch (SQLException e) {
            LOGGER.debug("UPDATE COMPUTER " + computer.getId() + ": " + e.getMessage());
        } catch (NoObjectException e) {
            LOGGER.debug("UPDATE COMPUTER NULL " + e.getMessage());
        }
        throw new InvalidComputerException("Une erreur s'est produite.");
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
        try {
            ComputerValidator.isValidId(id);
            if (computerDAO.delete(id)) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.debug("DELETE COMPUTER " + id + ": " + e.getMessage());
        }
        return false;
    }

    /**
     * Permet d'avoir le nombre de computers.
     * @return le nombre de computers
     */
    public int getCountComputers() {
        try {
            return computerDAO.count();
        } catch (SQLException e) {
            LOGGER.debug("ERREUR SQL COUNT " + e.getMessage());
        }
        return 0;
    }

    /**
     * Récupère le singleton de la façade.
     * @return le singleton Facade
     */
    public static Facade getInstance() {
        if (facade == null) {
            facade = new Facade();
        }
        return facade;
    }

}
