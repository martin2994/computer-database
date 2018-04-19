package main.java.com.excilys.cdb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.cdb.enums.DAOType;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.services.Facade;
import main.java.com.excilys.cdb.utils.Page;

/**
 * Controleur de l'application
 * Fait le lien entre la vue (CliUi) et le service (Facade)
 *
 */
public class CDBController {

	/**
	 * Facade de l'application
	 */
	private Facade facade;
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(CDBController.class);

	/**
	 * Constructeur qui assigne la facade au controleur
	 * @param facade la facade de l'application
	 */
	public CDBController(Facade facade) {
		this.facade = facade;
	}

	/**
	 * Permet de récupérer la liste des computer
	 * @return la liste des computers
	 */
	public Page<Computer> getComputers(String page) {
		return facade.getComputers(Integer.parseInt(page));
	}

	/**
	 * Permet de récupérer la liste des company
	 * @return la liste des company
	 */
	public Page<Company> getCompanies(String page) {
		return facade.getCompanies(Integer.parseInt(page));
	}

	/**
	 * Permet de supprimer un computer
	 * @param id l'id du computer à supprimer
	 */
	public void deleteCompute(String id) {
		facade.deleteCompute(Integer.parseInt(id));
	}

	/**
	 * Permet de créer un objet computer avec les champs adéquats
	 * @param computer l'objet initial ( pour l'update)
	 * @param name le nom du computer
	 * @param intro la date d'introduced du computer
	 * @param disco la date de discontinued du computer
	 * @param company_id l'id de la company du computer
	 * @return le computer avec les nouvelles informations
	 */
	public Computer fillComputer(Computer computer, String name, String intro, String disco, String company_id) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Computer newComputer = computer;
		try {
			if (!name.equals(""))
				newComputer.setName(name);
			Date introD=null, discoD=null;
			if (!intro.equals("")) {
				introD = format.parse(intro);
				newComputer.setIntroduced(introD);
			}
			if (!disco.equals("")) {
				discoD = format.parse(disco);
				newComputer.setDiscontinued(discoD);
			}
			if(discoD != null && introD != null) {
				if(discoD.before(introD)) return null;
			}
			if (!company_id.equals("")) {
				newComputer.setManufacturer(facade.getCompany(Integer.parseInt(company_id)));
			}else {
				newComputer.setManufacturer(null);
			}
		} catch (Exception e) {
			logger.debug("Date exception: "+ e);
		}
		System.out.println(newComputer);
		return newComputer;
	}
	
	/**
	 * Permet de modifier un computer
	 * @param computer_id l'id cu computer à modifier
	 * @param name le nom du computer
	 * @param intro la date d'introduced du computer
	 * @param disco la date de discontinued du computer
	 * @param company_id l'id de la company du computer
	 * @return booleen de mise à jour ou non du computer
	 */
	public boolean updateComputer(String computer_id, String name, String intro, String disco, String company_id) {
		Computer computerToUpdate = facade.getComputerDetails(Integer.parseInt(computer_id));
		Computer computerUpdate = fillComputer(computerToUpdate,name,intro,disco,company_id);
		if(null != computerUpdate){
			facade.updateComputer(computerUpdate);
			return true;
		}
		return false;
	}

	/**
	 * Permet de récupérer une company
	 * @param id l'id de la company
	 * @return le Company
	 */
	public Company getCompany(String id) {
		return facade.getCompany(Integer.parseInt(id));
	}

	/**
	 * Permet de récupérer les détails d'un computer
	 * @param id l'id du computer à voir
	 * @return le Computer
	 */
	public Computer getComputerDetails(String id) {
		return facade.getComputerDetails(Integer.parseInt(id));
	}

	/**
	 * Permet de créer le computer
	 * @param name le nom du computer
	 * @param intro la date d'introduced du computer
	 * @param disco la date de discontinued du computer
	 * @param company_id l'id de la company du computer
	 * @return booleen de création ou non du computer
	 */
	public boolean createComputer(String name, String intro, String disco, String company_id) {
		Computer computer = new Computer();
		Computer newComputer = fillComputer(computer, name, intro, disco, company_id);
		if(null != newComputer){
			facade.createComputer(newComputer);
			return true;
		}
		return false;
	}

	/**
	 * Permet de savoir si le computer existe
	 * @param computer_id l'id du computer à vérifier
	 * @return un booléen (true: existe, false: n'existe pas)
	 */
	public boolean isComputer(String computer_id) {
		return facade.getComputerDetails(Integer.parseInt(computer_id)) != null;
	}

	public String getMaxPage(DAOType type) {
		return facade.getMaxPage(type);
	}


}
