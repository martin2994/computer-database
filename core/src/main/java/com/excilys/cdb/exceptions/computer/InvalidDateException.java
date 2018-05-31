package com.excilys.cdb.exceptions.computer;

public class InvalidDateException extends InvalidComputerException {

    /**
     * ID.
     */
    private static final long serialVersionUID = -1841305071706900269L;

    /**
     * Constructeur avec message.
     * @param s
     *            le message
     */
    public InvalidDateException(String s) {
        super(s);
    }

}
