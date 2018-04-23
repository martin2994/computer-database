package com.excilys.cdb.exceptions;

/**
 * Exception concernant la création des DAO.
 * @author martin
 *
 */
public class NoDAOException extends Exception {

    /**
     * ID.
     */
    private static final long serialVersionUID = -5562822066103813976L;

    /**
     * Constructeur avec string.
     * @param s le message de l'erreur
     */
    public NoDAOException(String s) {
        super(s);
    }

}
