package com.excilys.cdb.mapper;

import java.util.Arrays;
import java.util.stream.Collectors;

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
     */
    public static String convertListId(String listId) {
        return Arrays.stream(listId.split(","))
                .map(stringId -> Long.parseLong(stringId))
                .collect(Collectors.toSet())
                .toString().replace("[", "(").replace("]", ")");
    }

}
