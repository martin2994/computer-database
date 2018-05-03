package com.excilys.cdb.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.exceptions.NoObjectException;
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
     * @throws SQLException
     *              Exception SQL lancée
     */
    List<T> findAll() throws SQLException;

    /**
     * Récupère une page d'objet de la base.
     * @param page
     *            le numéro de la page
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la page contenant les objets
     * @throws SQLException
     *             Exception liée à la requete
     */
    Page<T> findPerPage(int page, int resultPerPage) throws SQLException;

    /**
     * Récupère l'objet voulu.
     * @param id
     *            l'id de l'objet voulu
     * @return l'objet voulu
     * @throws SQLException
     *             Exception liée à la requete
     */
    Optional<T> findById(long id) throws SQLException;

    /**
     * Ajoute une objet dans la base.
     * @param t
     *            l'objet à ajouter
     * @return l'id du nouvelle objet
     * @throws SQLException
     *             Exception liée à la requete
     * @throws NoObjectException
     *             Exception lancée quand l'objet est null
     */
    long add(T t) throws SQLException, NoObjectException;

    /**
     * Supprime l'objet voulu.
     * @return Si la suppression a été effectuée
     * @param id
     *            l'id de l'objet à supprimer
     * @throws SQLException
     *             Exception liée à la requete
     */
    boolean delete(long id) throws SQLException;

    /**
     * Met à jour l'objet voulu.
     * @param t
     *            l'objet contenant les nouvelles informations
     * @return le nouvel objet
     * @throws SQLException
     *             Exception liée à la requete
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    Optional<T> update(T t) throws SQLException, NoObjectException;

    /**
     * Récupère le nombre de page maximum d'un table d'objet.
     * @return le nombre maximum de page
     * @throws SQLException
     *             Exception liée à la requete
     */
    int count() throws SQLException;

    /**
     * Regarde si l'objet existe.
     * @param id
     *            l'objet à verifier
     * @return un booleen avec la réponse
     * @throws SQLException
     *             Exception SQL lancée
     */
    boolean isExist(long id) throws SQLException;
}
