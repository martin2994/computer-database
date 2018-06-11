package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.excilys.cdb.clients.RestClient;
import com.excilys.cdb.dtos.ComputerDTO;
import com.excilys.cdb.enums.CompanyChoice;
import com.excilys.cdb.enums.ComputerChoice;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.enums.MenuChoice;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;

/**
 * Interface utilisateur CLI Regroupe toutes les fonctions d'affichage.
 *
 */

public class CliUi {

	/**
	 * Scanner pour lire les entrées clavier.
	 */
	private Scanner scanner;

	/**
	 * Booléen pour rester ou non dans le menu principal.
	 */
	private boolean whileMenu = true;

	/**
	 * Booléen pour rester ou non dans le menu des computers.
	 */
	private boolean computerWhile = true;

	/**
	 * Booléen pour rester ou non dans le menu des company.
	 */
	private boolean companyWhile = true;

	/**
	 * La page actuelle de l'utilisateur.
	 */
	private int currentPage;

	/**
	 * La page maximale.
	 */
	private int maxPage;
	
	private int resultPerPage = 10;

	private final String R_NUMBER = "[0-9]+";
	private final String R_TEXT = "[a-zA-Z-0-9]+";
	private final String R_DATE = "^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

	/**
	 * Constructeur pour attribuer le controler, le scanner et message d'arrivé.
	 * 
	 * @param context
	 *            le contexte de l'application
	 */
	public CliUi() {
		scanner = new Scanner(System.in);
		System.out.println("###################");
		System.out.println("######WELCOME######");
		System.out.println("###################");
		System.out.println();
		goToMenu();
	}

	/**
	 * Affiche le menu de l'application Redirige en fonction des actions choisies:
	 * MenuChoice.
	 */
	public void goToMenu() {
		while (whileMenu) {
			currentPage = 0;
			System.out.println("#############");
			System.out.println("####MENU####");
			System.out.println("#############");
			System.out.println();
			for (MenuChoice choice : MenuChoice.values()) {
				System.out.println(choice);
			}
			MenuChoice input = null;
			do {
				input = MenuChoice.get(scanner.nextLine());
			} while (input == null);
			switch (input) {
			case LISTCOMPUTER:
				showListComputers(RestClient.getComputers(currentPage, resultPerPage));
				break;
			case LISTCOMPANY:
				showListCompanies(RestClient.getCompanies(currentPage, resultPerPage));
				break;
			case QUIT:
				goToEnd();
				whileMenu = false;
				break;

			}
		}
	}

	/**
	 * Affiche la liste des computers et le sous-menu des computers Redirige en
	 * fonction des actions choisies: ComputerChoice.
	 * 
	 * @param computers
	 *            la liste des computers
	 */
	public void showListComputers(List<ComputerDTO> computers) {
		maxPage = RestClient.getCountComputers() / resultPerPage;
		System.out.println("###############");
		System.out.println("#COMPUTER MENU#");
		System.out.println("###############");
		System.out.println();
		for (ComputerDTO computer : computers) {
			System.out.println(computer);
		}
		System.out.println("Page " + currentPage + "/" + maxPage);
		System.out.println();
		while (computerWhile) {
			System.out.println("#############");
			System.out.println("###ACTIONS###");
			System.out.println("#############");
			System.out.println();
			for (ComputerChoice choice : ComputerChoice.values()) {
				System.out.println(choice);
			}
			ComputerChoice input = null;
			do {
				input = ComputerChoice.get(scanner.nextLine());
			} while (input == null);
			switch (input) {
			case SELECTPAGE:
				int page = selectPage(DAOType.COMPUTER);
				List<ComputerDTO> newPage = new ArrayList<>();
				newPage = RestClient.getComputers(page, resultPerPage);
				showListComputers(newPage);
				break;
			case CREATECOMPUTER:
				createComputer();
				break;
			case UPDATECOMPUTER:
				updateComputer();
				break;
			case DELETECOMPUTER:
				deleteComputer();
				break;
			case DETAILSCOMPUTER:
				showDetailsComputer();
				break;
			case BACKMENU:
				currentPage = 0;
				maxPage = 0;
				goToMenu();
			default:
				computerWhile = false;
				break;
			}
		}
	}

	/**
	 * Affiche la page max et courante.
	 * 
	 * @param type
	 *            le type d'objet voulu
	 * @return la page sélectionnée
	 */
	private int selectPage(DAOType type) {
		System.out.println("Your page: " + currentPage);
		System.out.println("Max page: " + maxPage);
		String page;
		do {
			page = scanner.nextLine();
		} while (!page.matches(R_NUMBER));
		return Integer.parseInt(page);
	}

	/**
	 * Affiche la liste des company Retourne sur le menu principal.
	 * 
	 * @param companies
	 *            la liste des company
	 */
	public void showListCompanies(List<Company> companies) {
		maxPage = RestClient.getCountCompanies() / resultPerPage;
		while (companyWhile) {
			System.out.println("###############");
			System.out.println("#COMPANY MENU#");
			System.out.println("###############");
			System.out.println();
			for (Company company : companies) {
				System.out.println(company);
			}
			System.out.println("Page " + currentPage + "/" + maxPage);
			System.out.println();
			for (CompanyChoice choice : CompanyChoice.values()) {
				System.out.println(choice);
			}
			CompanyChoice input = null;
			do {
				input = CompanyChoice.get(scanner.nextLine());
			} while (input == null);
			switch (input) {
			case SELECTPAGE:
				int page = selectPage(DAOType.COMPANY);
				List<Company> newPage = new ArrayList<>();
				newPage = RestClient.getCompanies(page, resultPerPage);
				showListCompanies(newPage);
				break;
			case DELETE:
				deleteCompany();
				break;
			case BACK:
				currentPage = 0;
				maxPage = 0;
				goToMenu();
			default:
				companyWhile = false;
				break;
			}
		}
	}

	/**
	 * Affiche les détails d'un computer.
	 */
	public void showDetailsComputer() {
		System.out.println("##################");
		System.out.println("#COMPUTER DETAILS#");
		System.out.println("##################");
		System.out.println();
		System.out.println("Computer id:");
		String id;
		do {
			id = scanner.nextLine();
		} while (!id.matches(R_NUMBER));
		ComputerDTO computerDTO = RestClient.getComputer(id);
		System.out.println(computerDTO);
	}

	/**
	 * Affichage de création d'un computer.
	 */
	public void createComputer() {
		System.out.println("#################");
		System.out.println("#CREATE COMPUTER#");
		System.out.println("#################");
		System.out.println();
		System.out.println("Tap ENTER if you don't want to specify this filed");
		System.out.println("Name:");
		String entry, name, intro, disco, companyId;
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_TEXT));
		name = entry;
		System.out.println("Introduced date:");
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_DATE) && !entry.equals(""));
		intro = entry;
		System.out.println("Discontinued date:");
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_DATE) && !entry.equals(""));
		disco = entry;
		System.out.println("Company id:");
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_NUMBER) && !entry.equals(""));
		companyId = entry;
		ComputerDTO newComputer = new ComputerDTO();
		try {
			newComputer = fillComputer(null, name, intro, disco, companyId);
			RestClient.createComputer(newComputer);
			System.out.println("CREATION EFFECTUEE DU COMPUTER ");
		} catch (InvalidComputerException e) {
			System.out.println("Une erreur est survenue à cause du nouveau computer.");
		} catch (InvalidCompanyException e) {
			System.out.println("Une erreur est survenue à cause de la company du nouveau computer.");
		} catch (NoObjectException e) {
			System.out.println("Mauvaise requete.");
		}
	}

	/**
	 * Affichage de mise à jour d'un computer.
	 */
	public void updateComputer() {
		System.out.println("#################");
		System.out.println("#UPDATE COMPUTER#");
		System.out.println("#################");
		System.out.println();
		System.out.println("Tap ENTER if you don't want to specify this filed");
		System.out.println("Computer id:");
		String entry, name, intro, disco, companyId;
		try {
			do {
				do {
					entry = scanner.nextLine();
				} while (!entry.matches(R_NUMBER));
			} while (!isComputer(entry));
			System.out.println("COMPUTER " + entry);
			ComputerDTO computerToUpdate = new ComputerDTO();
			computerToUpdate = RestClient.getComputer(entry);
			System.out.println(computerToUpdate);

			System.out.println("Name:");
			do {
				entry = scanner.nextLine();
			} while (!entry.matches(R_TEXT));
			name = entry;
			System.out.println("Introduced date:");
			do {
				entry = scanner.nextLine();
			} while (!entry.matches(R_DATE) && !entry.equals(""));
			intro = entry;
			System.out.println("Discontinued date:");
			do {
				entry = scanner.nextLine();
			} while (!entry.matches(R_DATE) && !entry.equals(""));
			disco = entry;
			System.out.println("Company id:");
			do {
				entry = scanner.nextLine();
			} while (!entry.matches(R_NUMBER) && !entry.equals(""));
			companyId = entry;
			ComputerDTO computerUpdate = fillComputer(computerToUpdate, name, intro, disco, companyId);
			RestClient.updateComputer(computerUpdate);
			System.out.println("MISE A JOUR EFFECTUEE");
		} catch (InvalidComputerException e) {
			System.out.println("MISE A JOUR NON EFFECTUEE");
			System.out.println("Une erreur est survenue à cause des infos du computer.");
		} catch (InvalidCompanyException e) {
			System.out.println("MISE A JOUR NON EFFECTUEE");
			System.out.println("Une erreur est survenue à cause de la company choisie.");
		} catch (NoObjectException e) {
			System.out.println("Mauvaise requete");
		}
	}

	/**
	 * Affichage de suppression d'un computer.
	 */
	public void deleteComputer() {
		System.out.println("#################");
		System.out.println("#DELETE COMPUTER#");
		System.out.println("#################");
		System.out.println();
		System.out.println("Computer id:");
		String entry = null;
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_NUMBER));
		RestClient.deleteComputer(entry);
		System.out.println("SUPPRESION EFFECTUEE");
	}

	/**
	 * Permet d'afficher la suppression d'une company.
	 */
	public void deleteCompany() {
		System.out.println("#################");
		System.out.println("#DELETE COMPANY#");
		System.out.println("#################");
		System.out.println();
		System.out.println("Company id:");
		String entry = null;
		do {
			entry = scanner.nextLine();
		} while (!entry.matches(R_NUMBER));
		RestClient.deleteCompany(entry);
		System.out.println("SUPPRESION EFFECTUEE");
	}

	/**
	 * Affichage de fin.
	 */
	public void goToEnd() {
		scanner.close();
		System.out.println("###################");
		System.out.println("########BYE########");
		System.out.println("###################");
	}

	/**
	 * Permet de savoir si le computer existe.
	 * 
	 * @param computerId
	 *            l'id du computer à vérifier
	 * @return un booléen (true: existe, false: n'existe pas)
	 * @throws InvalidComputerException
	 *             Exception lancée à cause des infos du computer
	 * @throws NoObjectException
	 *             Exception lancée quand la requete echoue (pas de resultat)
	 */
	public boolean isComputer(String computerId) throws InvalidComputerException, NoObjectException {
		return RestClient.getComputer(computerId) != null;
	}

	/**
	 * Permet de créer un objet computer avec les champs adéquats.
	 * 
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
	 * @throws InvalidComputerException
	 *             Exception lancée à cause des infos du computer
	 * @throws InvalidCompanyException
	 *             Exception lancée à cause de la company
	 * @throws NoObjectException
	 *             Exception lancée quand la requete échoue ( pas de resultat)
	 */
	public ComputerDTO fillComputer(ComputerDTO computer, String name, String intro, String disco, String companyId)
			throws InvalidComputerException, InvalidCompanyException, NoObjectException {
		String newName = null;
		if (!name.equals("")) {
			newName = name;
		} else {
			return null;
		}
		ComputerDTO newComputer = new ComputerDTO();
		if (computer != null) {
			newComputer = computer;
			newComputer.setName(newName);
		} else {
			newComputer = new ComputerDTO();
			newComputer.setName(newName);
		}
		if (!intro.equals("")) {
			newComputer.setIntroduced(intro);
		}
		if (!disco.equals("")) {
			newComputer.setDiscontinued(disco);
		}
		if (!StringUtils.isBlank(disco) && !StringUtils.isBlank(intro)) {
			if (LocalDate.parse(disco).isBefore(LocalDate.parse(intro))) {
				return null;
			}
		}
		if (!companyId.equals("")) {
			Company company = RestClient.getCompany(companyId);
			newComputer.setManufacturerId(company.getId());
			newComputer.setManufacturer(company.getName());
		} else {
			newComputer.setManufacturer(null);
		}
		System.out.println(newComputer);
		return newComputer;
	}

}
