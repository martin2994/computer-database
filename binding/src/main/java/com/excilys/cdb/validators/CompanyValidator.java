package com.excilys.cdb.validators;

import org.apache.commons.lang3.StringUtils;

import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;

public class CompanyValidator {

    /**
     * Vérifie si l'id est valide.
     * @param id
     *            l'id à vérifier
     * @throws InvalidCompanyException
     *             Exception sur les companies
     */
    public static void isValidId(long id) throws InvalidCompanyException {
        if (id < 0) {
            throw new InvalidCompanyException("L'id n'est pas valide.");
        }
    }
    
    public static void isValidCompany(Company company) throws InvalidCompanyException {
    	isValidId(company.getId());
    	if(StringUtils.isBlank(company.getName())) {
    		throw new InvalidCompanyException("Informations invalides");
    	}
    }
    
}
