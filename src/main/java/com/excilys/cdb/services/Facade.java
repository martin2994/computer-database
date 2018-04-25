package com.excilys.cdb.services;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

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
     * Récupère la liste des computer.
     * @param page
     *            la page à afficher
     * @return La liste des computer
     */
    public Page<Computer> getComputers(int page) {
        try {
            if (page >= 0) {
                return computerDAO.findAll(page);
            } else {
                LOGGER.info("INVALID COMPUTER PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPUTERS: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère la liste des company.
     * @param page
     *            la page à afficher
     * @return La liste des company
     */
    public Page<Company> getCompanies(int page) {
        try {
            if (page >= 0) {
                return companyDAO.findAll(page);
            } else {
                LOGGER.info("INVALID COMPANY PAGE");
            }
        } catch (SQLException e) {
            LOGGER.debug("FIND ALL COMPANIES: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère une company en fonction de son id.
     * @param id
     *            l'id de la company
     * @return la company
     */
    public Company getCompany(long id) {
        try {
            if (id > 0) {
                return companyDAO.findById(id);
            } else {
                LOGGER.info("INVALID COMPANY ID FOR DETAILS");
            }
        } catch (SQLException e) {
            LOGGER.debug("GET COMPANY " + id + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère un computer spécifique.
     * @param id
     *            l'id du computer à chercher
     * @return le computer
     */
    public Computer getComputerDetails(long id) {
        try {
            if (id > 0) {
                return computerDAO.findById(id);
            } else {
                LOGGER.info("INVALID COMPUTER ID FOR DETAILS");
            }
        } catch (SQLException e) {
            LOGGER.debug("GET COMPUTER " + id + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Crée un computer.
     * @param computer
     *            le computer à créer
     * @return l'id du nouveau computer
     */
    public long createComputer(Computer computer) {
        try {
            if (computer == null) {
                LOGGER.info("INVALID COMPUTER FOR CREATE");
                return 0;
            }
            if (computer.getName() == null) {
                LOGGER.info("INVALID NAME FOR CREATE");
                return 0;
            }
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR CREATE");
                    return 0;
                }
            }
            if (computer.getDiscontinued() != null && computer.getIntroduced() != null) {
                if (computer.getDiscontinued().isBefore(computer.getIntroduced())) {
                    LOGGER.info("INVALID DATE COMPUTER FOR CREATE");
                    return 0;
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
     */
    public Computer updateComputer(Computer computer) {
        try {
            if (computer == null) {
                LOGGER.info("INVALID COMPUTER FOR UPDATE");
                return null;
            }
            if (computer.getName() == null) {
                LOGGER.info("INVALID NAME FOR UPDATE");
                return null;
            }
            if (!computerDAO.isExist(computer.getId())) {
                LOGGER.info("INVALID COMPUTER FOR UPDATE");
                return null;
            }
            if (computer.getManufacturer() != null) {
                if (!companyDAO.isExist(computer.getManufacturer().getId())) {
                    LOGGER.info("INVALID COMPANY FOR UPDATE");
                    return null;
                }
            }
            if (computer.getDiscontinued() != null && computer.getIntroduced() != null) {
                if (computer.getDiscontinued().isBefore(computer.getIntroduced())) {
                    LOGGER.info("INVALID DATE COMPUTER FOR UPDATE");
                    return null;
                }
            }
            return computerDAO.update(computer);

        } catch (SQLException e) {
            LOGGER.debug("UPDATE COMPUTER " + computer.getId() + ": " + e.getMessage());
        } catch (NoObjectException e) {
            LOGGER.debug("UPDATE COMPUTER NULL " + e.getMessage());
        }
        return null;
    }

    /**
     * Supprime un computer.
     * @param id
     *            l'id du computer à supprimer
     * @return Si la suppression a été effectuée
     */
    public boolean deleteComputer(long id) {
        try {
            if (id > 0) {
                if (computerDAO.delete(id)) {
                    return true;
                }
            } else {
                LOGGER.info("INVALID COMPUTER ID FOR DELETE");
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
