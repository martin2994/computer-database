package com.excilys.cdb.controller;

import java.time.LocalDate;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.Facade;
import com.excilys.cdb.utils.Page;

/**
 * Controleur de l'application Fait le lien entre la vue (CliUi) et le service
 * (Facade).
 *
 */
public class CDBController {

    /**
     * Facade de l'application.
     */
    private Facade facade;

    /**
     * Constructeur qui assigne la facade au controleur.
     * @param facade
     *            la facade de l'application
     */
    public CDBController(Facade facade) {
        this.facade = facade;
    }

    /**
     * Permet de récupérer la liste des computer.
     * @param page
     *            la page à afficher
     * @return la liste des computers
     */
    public Page<Computer> getComputers(int page) {
        return facade.getComputers(page);
    }

    /**
     * Permet de récupérer la liste des company.
     * @param page
     *            la page à afficher
     * @return la liste des company
     */
    public Page<Company> getCompanies(int page) {
        return facade.getCompanies(page);
    }

    /**
     * Permet de supprimer un computer.
     * @param id
     *            l'id du computer à supprimer
     */
    public void deleteComputer(String id) {
        facade.deleteComputer(Long.parseLong(id));
    }

    /**
     * Permet de créer un objet computer avec les champs adéquats.
     * @param computer
     *            l'objet initial ( pour l'update)
     * @param name
     *            le nom du computer
     * @param intro
     *            la date d'introduced du computer
     * @param disco
     *            la date de discontinued du computer
     * @param companyId
     *            l'id de la company du computer
     * @return le computer avec les nouvelles informations
     */
    public Computer fillComputer(Computer computer, String name, String intro, String disco, String companyId) {
        String newName = null;
        if (!name.equals("")) {
            newName = name;
        } else {
            return null;
        }
        Computer newComputer = null;
        if (computer != null) {
            newComputer = computer;
            newComputer.setName(newName);
        } else {
            newComputer = new Computer.Builder(newName).build();
        }
        LocalDate introD = null, discoD = null;
        if (!intro.equals("")) {
            introD = LocalDate.parse(intro);
            newComputer.setIntroduced(introD);
        }
        if (!disco.equals("")) {
            discoD = LocalDate.parse(disco);
            newComputer.setDiscontinued(discoD);
        }
        if (discoD != null && introD != null) {
            if (discoD.isBefore(introD)) {
                return null;
            }
        }
        if (!companyId.equals("")) {
            newComputer.setManufacturer(facade.getCompany(Integer.parseInt(companyId)));
        } else {
            newComputer.setManufacturer(null);
        }
        System.out.println(newComputer);
        return newComputer;
    }

    /**
     * Permet de modifier un computer.
     * @param computerId
     *            l'id cu computer à modifier
     * @param name
     *            le nom du computer
     * @param intro
     *            la date d'introduced du computer
     * @param disco
     *            la date de discontinued du computer
     * @param companyId
     *            l'id de la company du computer
     * @return booleen de mise à jour ou non du computer
     */
    public boolean updateComputer(String computerId, String name, String intro, String disco, String companyId) {
        Computer computerToUpdate = facade.getComputerDetails(Integer.parseInt(computerId));
        Computer computerUpdate = fillComputer(computerToUpdate, name, intro, disco, companyId);
        if (null != computerUpdate) {
            if (facade.updateComputer(computerUpdate) != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Permet de récupérer une company.
     * @param id
     *            l'id de la company
     * @return le Company
     */
    public Company getCompany(String id) {
        return facade.getCompany(Long.parseLong(id));
    }

    /**
     * Permet de récupérer les détails d'un computer.
     * @param id
     *            l'id du computer à voir
     * @return le Computer
     */
    public Computer getComputerDetails(String id) {
        return facade.getComputerDetails(Long.parseLong(id));
    }

    /**
     * Permet de créer le computer.
     * @param name
     *            le nom du computer
     * @param intro
     *            la date d'introduced du computer
     * @param disco
     *            la date de discontinued du computer
     * @param companyId
     *            l'id de la company du computer
     * @return booleen de création ou non du computer
     */
    public long createComputer(String name, String intro, String disco, String companyId) {
        Computer newComputer = fillComputer(null, name, intro, disco, companyId);
        if (null != newComputer) {
            return facade.createComputer(newComputer);
        }
        return 0;
    }

    /**
     * Permet de savoir si le computer existe.
     * @param computerId
     *            l'id du computer à vérifier
     * @return un booléen (true: existe, false: n'existe pas)
     */
    public boolean isComputer(String computerId) {
        return facade.getComputerDetails(Integer.parseInt(computerId)) != null;
    }

}
