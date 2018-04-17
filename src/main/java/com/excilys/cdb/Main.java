package main.java.com.excilys.cdb;

import main.java.com.excilys.cdb.controller.CDBController;
import main.java.com.excilys.cdb.services.Facade;
import main.java.com.excilys.cdb.ui.CliUi;

public class Main {
	public static void main(String[] args) {
			Facade facade = new Facade();
			CDBController cdbController = new CDBController(facade);
			CliUi cliUi = new CliUi(cdbController);
	}

}
