package com.excilys.cdb.exceptions.company;

public class InvalidCompanyException extends Exception {

    /**
     * ID.
     */
    private static final long serialVersionUID = -2595841819413944816L;

    /**
     * Constructeur avec message.
     * @param s
     *            le message
     */
    public InvalidCompanyException(String s) {
        super(s);
    }

}
