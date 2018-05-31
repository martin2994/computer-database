package com.excilys.cdb.validators;

import com.excilys.cdb.exceptions.company.InvalidCompanyException;

public class CompanyValidator {

    /**
     * Vérifie si l'id est valide.
     * @param id
     *            l'id à vérifier
     * @throws InvalidCompanyException
     *             Exception sur les companies
     */
    public static void isValidId(long id) throws InvalidCompanyException {
        if (id <= 0) {
            throw new InvalidCompanyException("L'id n'est pas valide.");
        }
    }
}
