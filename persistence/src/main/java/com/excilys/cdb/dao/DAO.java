package com.excilys.cdb.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.utils.Page;

/**
 * Interface regroupant toutes les transactions sur la BD.
 * @param <T>
 *            L'objet manipulé
 */
public interface DAO<T> {

    /**
     * Récupère la liste complète des objets.
     * @return la liste complète
     */
    List<T> findAll();

    /**
     * Récupère une page d'objet de la base.
     * @param page
     *            le numéro de la page
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la page contenant les objets
     * @throws Exception
     *          Exception lancée en cas d'erreur
     */
    Page<T> findPerPage(int page, int resultPerPage) throws Exception;

    /**
     * Récupère l'objet voulu.
     * @param id
     *            l'id de l'objet voulu
     * @return l'objet voulu
     */
    Optional<T> findById(long id);

    /**
     * Ajoute une objet dans la base.
     * @param t
     *            l'objet à ajouter
     * @return l'id du nouvelle objet
     * @throws NoObjectException
     *             Exception lancée quand l'objet est vide
     * @throws InvalidComputerException 
     */
    long add(T t) throws NoObjectException, InvalidComputerException;

    /**
     * Supprime l'objet voulu.
     * @return Si la suppression a été effectuée
     * @param id
     *            l'id de l'objet à supprimer
     */
    boolean delete(long id);

    /**
     * Met à jour l'objet voulu.
     * @param t
     *            l'objet contenant les nouvelles informations
     * @return le nouvel objet
     * @throws SQLException
     *             Exception liée à la requete
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidComputerException 
     * 				Exception lancé quand les infos du computer sont invalides.
     */
    Optional<T> update(T t) throws NoObjectException, InvalidComputerException;

    /**
     * Récupère le nombre de page maximum d'un table d'objet.
     * @return le nombre maximum de page
     */
    int count();

    /**
     * Regarde si l'objet existe.
     * @param id
     *            l'objet à verifier
     * @return un booleen avec la réponse
     */
    boolean isExist(long id);
}
