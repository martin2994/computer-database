package main.java.com.excilys.cdb.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import main.java.com.excilys.cdb.controller.CDBController;
import main.java.com.excilys.cdb.enums.ComputerChoice;
import main.java.com.excilys.cdb.enums.MenuChoice;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
public class CliUi {

	private Scanner scanner;
	private boolean whileMenu = true;
	private boolean computerWhile = true;
	private CDBController controller;
	
	public CliUi(CDBController cdbController) {
		controller = cdbController;
		scanner = new Scanner(System.in);
		System.out.println("###################");
		System.out.println("######WELCOME######");
		System.out.println("###################");
		System.out.println();
		goToMenu();
	}

	public void goToMenu() {
		while (whileMenu) {
			System.out.println("#############");
			System.out.println("###ACTIONS###");
			System.out.println("#############");
			System.out.println();
			System.out.println(MenuChoice.LISTCOMPUTER);
			System.out.println(MenuChoice.LISTCOMPANY);
			System.out.println(MenuChoice.QUIT);
			MenuChoice input = null;
			do {
				input = MenuChoice.get(scanner.nextLine());
			} while (input == null);
			switch (input) {
			case LISTCOMPUTER:
				showListComputers(controller.getComputers());
				break;
			case LISTCOMPANY:
				showListCompanies(controller.getCompanies());
				break;
			case QUIT:
				goToEnd();
				whileMenu = false;
				break;

			}
		}
	}

	public void showListComputers(List<Computer> computers) {
		System.out.println("###############");
		System.out.println("#COMPUTER LIST#");
		System.out.println("###############");
		System.out.println();
		for (Computer computer : computers) {
			System.out.println(computer);
		}
		while (computerWhile) {
			System.out.println("#############");
			System.out.println("###ACTIONS###");
			System.out.println("#############");
			System.out.println();
			System.out.println(ComputerChoice.DETAILSCOMPUTER);
			System.out.println(ComputerChoice.CREATECOMPUTER);
			System.out.println(ComputerChoice.UPDATECOMPUTER);
			System.out.println(ComputerChoice.DELETECOMPUTER);
			System.out.println(ComputerChoice.SHOWLIST);
			System.out.println(ComputerChoice.BACKMENU);
			ComputerChoice input = null;
			do {
				input = ComputerChoice.get(scanner.nextLine());
			} while (input == null);
			switch (input) {
			case CREATECOMPUTER:
				createComputer();
				break;
			case UPDATECOMPUTER:
				updateComputer();
				break;
			case DELETECOMPUTER:
				deleteComputer();
				break;
			case SHOWLIST:
				showListComputers(controller.getComputers());
				break;
			case DETAILSCOMPUTER:
				showDetailsComputer();
				break;
			case BACKMENU:
				goToMenu();
			default:
				computerWhile = false;
				break;
			}
		}
	}

	public void showListCompanies(List<Company> companies) {
		System.out.println("###############");
		System.out.println("#COMPANY LIST#");
		System.out.println("###############");
		System.out.println();
		for (Company company : companies) {
			System.out.println(company);
		}
	}

	public void showDetailsComputer() {
		System.out.println("##################");
		System.out.println("#COMPUTER DETAILS#");
		System.out.println("##################");
		System.out.println();
		System.out.println("Computer id:");
		String id;
		do {
			id = scanner.nextLine();
		} while (!id.matches("[0-9]+"));
		Computer computer = controller.getComputerDetails(Integer.parseInt(id));
		System.out.println(computer);
	}

	public void createComputer() {
		try {
			System.out.println("#################");
			System.out.println("#CREATE COMPUTER#");
			System.out.println("#################");
			System.out.println();
			System.out.println("Tap ENTER if you don't want to specify this filed");
			System.out.println("Name:");
			Computer computer = new Computer();
			String s = null;
			Date d = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			do {
				s = scanner.nextLine();
			} while (!s.matches("[a-zA-Z-0-9]+") && !s.equals(""));
			computer.setName(s);
			System.out.println("Introduced date:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])") && !s.equals(""));
			if (!s.equals("")) {
				d = format.parse(s);
				computer.setIntroduced(d);
			}
			System.out.println("Discontinued date:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])") && !s.equals(""));
			if (!s.equals("")) {
				d = format.parse(s);
				computer.setDiscontinued(d);
			}
			System.out.println("Company id:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("[0-9]+") && !s.equals(""));
			if(!s.equals("")) {
				computer.setManufacturer(controller.getCompany(Integer.parseInt(s)));
			}
			controller.createComputer(computer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateComputer() {
		try {
			System.out.println("#################");
			System.out.println("#UPDATE COMPUTER#");
			System.out.println("#################");
			System.out.println();
			System.out.println("Tap ENTER if you don't want to specify this filed");
			System.out.println("Computer id:");
			String s = null;
			Computer computerUpdate = null;
			Date d = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			do {
				do {
					s = scanner.nextLine();
				} while (!s.matches("[0-9]+"));
				computerUpdate = controller.getComputerDetails(Integer.parseInt(s));
			} while (computerUpdate == null);
			System.out.println("Name:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("[a-zA-Z-0-9]+") && !s.equals(""));
			if (!s.equals(""))
				computerUpdate.setName(s);
			System.out.println("Introduced date:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])") && !s.equals(""));
			if (!s.equals("")) {
				d = format.parse(s);
				computerUpdate.setIntroduced(d);
			}
			System.out.println("Discontinued date:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("^((18|19|20|21)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])") && !s.equals(""));
			if (!s.equals("")) {
				d = format.parse(s);
				computerUpdate.setDiscontinued(d);
			}
			System.out.println("Company id:");
			do {
				s = scanner.nextLine();
			} while (!s.matches("[0-9]+") && !s.equals(""));
			if (!s.equals(""))
				computerUpdate.setManufacturer(controller.getCompany(Integer.parseInt(s)));
			controller.updateComputer(computerUpdate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteComputer() {
		System.out.println("#################");
		System.out.println("#DELETE COMPUTER#");
		System.out.println("#################");
		System.out.println();
		System.out.println("Computer id:");
		String s = null;
		do {
			s = scanner.nextLine();
		} while (!s.matches("[0-9]+"));
		controller.deleteCompute(Integer.parseInt(s));
	}

	public void goToEnd() {
		System.out.println("###################");
		System.out.println("########BYE########");
		System.out.println("###################");
	}

	@Override
	protected void finalize() throws Throwable {
		scanner.close();
	}
}
