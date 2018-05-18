package com.excilys.cdb.services;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
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
    @Autowired
    private CompanyDAO companyDAO;

    /**
     * DAO de computer.
     */
    @Autowired
    private ComputerDAO computerDAO;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);

    /**
     * Constructeur privé.
     */
    private ComputerService() {
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
        Page<Computer> cPage = new Page<>();
        try {
            if (page >= 0 && resultPerPage >= 1) {
                cPage = computerDAO.findPerPage(page, resultPerPage);
            } else {
                LOGGER.info("INVALID COMPUTER PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPUTERS: " + e.getMessage());
        }
        return cPage;
    }

    /**
     * Récupère la liste des computer triée et recherchée par page.
     * @param search
     *            le nom à rechercher
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            le nombre de computer par page
     * @return La liste des computer
     */
    public Page<Computer> getComputersByName(String search, int page, int resultPerPage) {
        Page<Computer> cPage = new Page<>();
        try {
            if (page >= 0 && resultPerPage >= 1) {
                cPage = computerDAO.findByNamePerPage(search, page, resultPerPage);
            } else {
                LOGGER.info("INVALID COMPUTER PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND BY NAME COMPUTERS: " + e.getMessage());
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
        long result = 0;
        try {
            ComputerValidator.isValidComputer(computer);
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE COMPUTER");
                    throw new InvalidCompanyException("La company n'existe pas.");
                }
            }
            result = computerDAO.add(computer);
        } catch (SQLException e) {
            LOGGER.debug("CREATE COMPUTER " + computer.getId() + ": " + e.getMessage());
        } catch (NoObjectException e) {
            LOGGER.debug("CREATE COMPUTER WITH NULL OBJECT" + e.getMessage());
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
        boolean result = false;
        try {
            ComputerValidator.isValidId(id);
            if (computerDAO.delete(id)) {
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.debug("DELETE COMPUTER " + id + ": " + e.getMessage());
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
        try {
            if (computerDAO.deleteList(idList)) {
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.debug("DELETE COMPUTER LIST: " + e.getMessage());
        }
        return result;
    }

    /**
     * Permet d'avoir le nombre de computers.
     * @return le nombre de computers
     */
    public int getCountComputers() {
        int result = 0;
        try {
            result = computerDAO.count();
        } catch (SQLException e) {
            LOGGER.debug("ERREUR SQL COUNT " + e.getMessage());
        }
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
        try {
            result = computerDAO.countByName(search);
        } catch (SQLException e) {
            LOGGER.debug("ERREUR SQL COUNT " + e.getMessage());
        }
        return result;
    }

}
