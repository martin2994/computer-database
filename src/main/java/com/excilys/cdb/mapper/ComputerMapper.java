package com.excilys.cdb.mapper;

import java.util.Set;

import com.excilys.cdb.exceptions.computer.InvalidIdException;

/**
 * Possède les différentes fonction de conversion d'un computer SQL vers un
 * computer du modèle.
 * @author martin
 *
 */
public class ComputerMapper {

    /**
     * Permet de transformer une liste d'id et l'avoir dans le bon format.
     * @param listId
     *            la liste des id en string
     * @return la liste sous le bon format
     * @throws InvalidIdException
     *             Exception lancée quand la liste d'id est invalide.
     */
    public static String convertListId(Set<Long> listId) throws InvalidIdException {
        try {
            if (listId.isEmpty()) {
                throw new InvalidIdException("La liste d'ids est vide");
            }
            return listId.toString().replace("[", "(").replace("]", ")");
        } catch (NumberFormatException e) {
            throw new InvalidIdException("La liste d'ids est invalide");
        }
    }

}
