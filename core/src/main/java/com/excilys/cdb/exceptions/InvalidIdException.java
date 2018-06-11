package com.excilys.cdb.exceptions;

public class InvalidIdException extends Exception {

    /**
     * ID.
     */
    private static final long serialVersionUID = -8386442659099681453L;

    /**
     * Constructeur avec message.
     * @param s
     *            le message
     */
    public InvalidIdException(String s) {
        super(s);
    }

}
