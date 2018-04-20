package com.excilys.cdb;

import com.excilys.cdb.controller.CDBController;
import com.excilys.cdb.services.Facade;
import com.excilys.cdb.ui.CliUi;

public class Main {
	public static void main(String[] args) {
			Facade facade = Facade.getInstance();
			CDBController cdbController = new CDBController(facade);
			CliUi cliUi = new CliUi(cdbController);
	}

}
