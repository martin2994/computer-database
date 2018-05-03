package com.excilys.cdb;

import com.excilys.cdb.services.Facade;
import com.excilys.cdb.ui.CliUi;

public class Main {
    /**
     * la fonction main.
     * @param args
     *            les arguments de l'appel
     */
    public static void main(String[] args) {
        Facade facade = Facade.getInstance();
        CliUi cliUi = new CliUi(facade);
    }

}
