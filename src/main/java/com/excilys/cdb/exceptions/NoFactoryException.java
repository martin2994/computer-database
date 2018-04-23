package com.excilys.cdb.exceptions;

/**
 * Exception concernant la création de la DAOFactory.
 * @author martin
 *
 */
public class NoFactoryException extends Exception {
    /**
     * ID.
     */
    private static final long serialVersionUID = -624937730098602318L;

    /**
     * Constructeur avec string.
     * @param s
     *            le message de l'erreur
     */
    public NoFactoryException(String s) {
        super(s);
    }
}
