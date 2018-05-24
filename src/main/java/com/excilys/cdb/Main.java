package com.excilys.cdb;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.excilys.cdb.config.SpringConfigCLI;
import com.excilys.cdb.ui.CliUi;

public class Main {
    /**
     * la fonction main.
     * @param args
     *            les arguments de l'appel
     */
    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigCLI.class);
        CliUi cliUi = new CliUi(context);
    }

}
