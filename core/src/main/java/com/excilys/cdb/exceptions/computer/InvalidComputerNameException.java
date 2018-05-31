package com.excilys.cdb.exceptions.computer;

public class InvalidComputerNameException extends InvalidComputerException {

    /**
     * ID.
     */
    private static final long serialVersionUID = -4896485825636831189L;

    /**
     * Constructeur avec message.
     * @param s
     *            le message
     */
    public InvalidComputerNameException(String s) {
        super(s);
    }

}
