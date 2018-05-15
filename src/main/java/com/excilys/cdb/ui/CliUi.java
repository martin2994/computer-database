package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.excilys.cdb.enums.CompanyChoice;
import com.excilys.cdb.enums.ComputerChoice;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.enums.MenuChoice;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.CompanyService;
import com.excilys.cdb.services.ComputerService;
import com.excilys.cdb.utils.Page;

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
    private Page<?> currentPage;

    /**
     * Le service des computers.
     */
    private ComputerService computerService;

    /**
     * Le service des companies.
     */
    private CompanyService companyService;

    private final String R_NUMBER = "[0-9]+";
    private final String R_TEXT = "[a-zA-Z-0-9]+";
    private final String R_DATE = "^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    /**
     * Constructeur pour attribuer le controler, le scanner et message d'arrivé.
     */
    public CliUi() {
        companyService = CompanyService.getInstance();
        computerService = ComputerService.getInstance();
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
            currentPage = new Page<>();
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
                showListComputers(computerService.getComputers(currentPage.getCurrentPage(), 10));
                break;
            case LISTCOMPANY:
                showListCompanies(companyService.getCompanies(currentPage.getCurrentPage(), 10));
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
     * @param computers
     *            la liste des computers
     */
    public void showListComputers(Page<Computer> computers) {
        currentPage = computers;
        System.out.println("###############");
        System.out.println("#COMPUTER MENU#");
        System.out.println("###############");
        System.out.println();
        for (Computer computer : computers.getResults()) {
            System.out.println(computer);
        }
        System.out.println("Page " + computers.getCurrentPage() + "/" + computers.getMaxPage());
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
                Page<Computer> newPage = computerService.getComputers(page, 5);
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
                currentPage = null;
                goToMenu();
            default:
                computerWhile = false;
                break;
            }
        }
    }

    /**
     * Affiche la page max et courante.
     * @param type
     *            le type d'objet voulu
     * @return la page sélectionnée
     */
    private int selectPage(DAOType type) {
        System.out.println("Your page: " + currentPage.getCurrentPage());
        System.out.println("Max page: " + currentPage.getMaxPage());
        String page;
        do {
            page = scanner.nextLine();
        } while (!page.matches(R_NUMBER));
        return Integer.parseInt(page);
    }

    /**
     * Affiche la liste des company Retourne sur le menu principal.
     * @param companies
     *            la liste des company
     */
    public void showListCompanies(Page<Company> companies) {
        currentPage = companies;
        while (companyWhile) {
            System.out.println("###############");
            System.out.println("#COMPANY MENU#");
            System.out.println("###############");
            System.out.println();
            for (Company company : companies.getResults()) {
                System.out.println(company);
            }
            System.out.println("Page " + companies.getCurrentPage() + "/" + companies.getMaxPage());
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
                Page<Company> newPage = companyService.getCompanies(page, 5);
                showListCompanies(newPage);
                break;
            case DELETE:
                deleteCompany();
                break;
            case BACK:
                currentPage = null;
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
        Computer computer;
        try {
            computer = computerService.getComputerDetails(Long.parseLong(id));
            System.out.println(computer);
        } catch (InvalidComputerException e) {
            System.out.println("Le computer choisi n'existe pas.");
        }
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
        Computer newComputer = null;
        long newId;
        try {
            newComputer = fillComputer(null, name, intro, disco, companyId);
            newId = computerService.createComputer(newComputer);
            if (newId > 0) {
                System.out.println("CREATION EFFECTUEE DU COMPUTER " + newId);
            } else {
                System.out.println("CREATION NON EFFECTUEE");
            }
        } catch (InvalidComputerException e) {
            System.out.println("Une erreur est survenue à cause du nouveau computer.");
        } catch (InvalidCompanyException e) {
            System.out.println("Une erreur est survenue à cause de la company du nouveau computer.");
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
            Computer computerToUpdate = null;
            computerToUpdate = computerService.getComputerDetails(Long.parseLong(entry));
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
            Computer computerUpdate = fillComputer(computerToUpdate, name, intro, disco, companyId);
            computerService.updateComputer(computerUpdate);
            System.out.println("MISE A JOUR EFFECTUEE");
        } catch (InvalidComputerException e) {
            System.out.println("MISE A JOUR NON EFFECTUEE");
            System.out.println("Une erreur est survenue à cause des infos du computer.");
        } catch (InvalidCompanyException e) {
            System.out.println("MISE A JOUR NON EFFECTUEE");
            System.out.println("Une erreur est survenue à cause de la company choisie.");
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
        try {
            computerService.deleteComputer(Long.parseLong(entry));
            System.out.println("SUPPRESION EFFECTUEE");
        } catch (InvalidIdException e) {
            System.out.println("SUPPRESION NON EFFECTUEE: Erreur à cause du computer choisi");
        }
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
        try {
            companyService.deleteCompany(Long.parseLong(entry));
            System.out.println("SUPPRESION EFFECTUEE");
        } catch (InvalidCompanyException e) {
            System.out.println("SUPPRESION NON EFFECTUEE: Erreur à cause de la company choisi");
        }
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
     * @param computerId
     *            l'id du computer à vérifier
     * @return un booléen (true: existe, false: n'existe pas)
     * @throws InvalidComputerException
     *             Exception lancée à cause des infos du computer
     */
    public boolean isComputer(String computerId) throws InvalidComputerException {
        return computerService.getComputerDetails(Integer.parseInt(computerId)) != null;
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
     * @throws InvalidComputerException
     *             Exception lancée à cause des infos du computer
     * @throws InvalidCompanyException
     *             Exception lancée à cause de la company
     */
    public Computer fillComputer(Computer computer, String name, String intro, String disco, String companyId)
            throws InvalidComputerException, InvalidCompanyException {
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
            newComputer.setManufacturer(companyService.getCompany(Integer.parseInt(companyId)));
        } else {
            newComputer.setManufacturer(null);
        }
        System.out.println(newComputer);
        return newComputer;
    }

}
