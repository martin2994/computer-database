package com.excilys.cdb.exceptions;

/**
 * Exception lancée dans le cas d'un ajout ou mise à jour d'un computer.
 * @author martin
 *
 */
public class NoObjectException extends Exception {

    /**
     * ID.
     */
    private static final long serialVersionUID = 3562006366551124263L;

    /**
     * Constructeur avec message d'erreur.
     * @param s
     *            le message d'erreur
     */
    public NoObjectException(String s) {
        super(s);
    }
}
