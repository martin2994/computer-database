package com.excilys.cdb.validators;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidComputerNameException;
import com.excilys.cdb.exceptions.computer.InvalidDateException;
import com.excilys.cdb.model.Computer;

public class ComputerValidator {

    /**
     * Vérifie si l'id est valide.
     * @param id
     *            l'id à vérifier
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    public static void isValidId(long id) throws InvalidIdException {
        if (id < 0) {
            throw new InvalidIdException("L'id n'est pas valide.");
        }
    }

    /**
     * Vérifie si le nom est valide.
     * @param name
     *            Le nom à vérifier
     * @throws InvalidComputerNameException
     *             Exception pour le nom lancée
     */
    public static void isValidName(String name) throws InvalidComputerNameException {
        if (StringUtils.isBlank(name)) {
            throw new InvalidComputerNameException("Le nom n'est pas valide.");
        }
    }

    /**
     * Vérifie si le computer est valide.
     * @param computer
     *            le computer à vérifier
     * @throws InvalidComputerException
     *             Exception sur les computers
     * @throws InvalidIdException 
     * 				Excpetion sur les ids
     */
    public static void isValidComputer(Computer computer) throws InvalidComputerException, InvalidIdException {
        if (computer == null) {
            throw new InvalidComputerException("Il n'y a pas de computer.");
        }
        isValidId(computer.getId());
        isValidName(computer.getName());
        isValidDates(computer.getIntroduced(), computer.getDiscontinued());
    }

    /**
     * Vérifie si les dates sont valides.
     * @param intro
     *            la date d'introduced à vérifier
     * @param disco
     *            la date de discontinued à vérifier
     * @throws InvalidDateException
     *             Exception concernant les dates d'un computer
     */
    public static void isValidDates(LocalDate intro, LocalDate disco) throws InvalidDateException {
        if (intro != null && disco != null) {
            if (intro.isAfter(disco)) {
                throw new InvalidDateException("Les dates ne sont pas valides.");
            }
        }
    }

}
