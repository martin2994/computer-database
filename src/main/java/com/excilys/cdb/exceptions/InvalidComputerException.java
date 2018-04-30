package com.excilys.cdb.exceptions;

public class InvalidComputerException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 2462171998763223503L;

    /**
     * Constructeur avec message d'erreur.
     * @param s
     *            le message d'erreur
     */
    public InvalidComputerException(String s) {
        super(s);
    }
}
