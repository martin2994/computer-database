package com.excilys.cdb.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * DAO pour Company Regroupe les différentes transactions sur les Company.
 */
public class CompanyDAO implements DAO<Company> {

    /**
     * Connexion à la BD.
     */
    private Connection connection;

    /**
     * Transaction en cours.
     */
    private PreparedStatement statement;

    /**
     * Requete pour le findAll.
     */
    private final String ALL_COMPANIES = "SELECT id,name FROM company LIMIT ?,?";

    /**
     * Requete pour le findById.
     */
    private final String COMPANY_BY_ID = "SELECT id,name FROM company WHERE id=?";

    /**
     * Requete pour le nombre de page.
     */
    private final String MAX_PAGE = "SELECT COUNT(id) FROM company";

    /**
     * le singleton.
     */
    private static CompanyDAO companyDAO;

    /**
     * Constructeur pour initialiser la connexion.
     * @param connection
     *            La connexion en cours
     */
    private CompanyDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Permet de récupérer la liste de toutes les company.
     * @return La liste des Company
     */
    @Override
    public Page<Company> findAll(int page) throws SQLException {
        Page<Company> companies = new Page<>();
        statement = connection.prepareStatement(ALL_COMPANIES);
        statement.setInt(1, page * Page.RESULT_PER_PAGE);
        statement.setInt(2, Page.RESULT_PER_PAGE);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            companies.add(new Company(rs.getInt("id"), rs.getString("name")));
        }
        rs.close();
        statement.close();
        companies.setCurrentPage(page);
        companies.setMaxPage(getMaxPage());
        return companies;
    }

    /**
     * Récupère une company particulière.
     * @param id
     *            , l'id de la company à rechercher
     * @return La company correspondante
     */
    @Override
    public Company findById(long id) throws SQLException {
        statement = connection.prepareStatement(COMPANY_BY_ID);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return new Company(rs.getInt("id"), rs.getString("name"));
        }
        return null;
    }

    /**
     * Récupère le nombre de page total.
     */
    @Override
    public int getMaxPage() throws SQLException {
        statement = connection.prepareStatement(MAX_PAGE);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) / Page.RESULT_PER_PAGE;
        }
        return 0;
    }

    @Override
    public int add(Company t) {
        return 0;
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public Computer update(Company t) {
        return null;
    }

    /**
     * Récupère le singleton de companyDao.
     * @param connection
     *            la connexion à la BD
     * @return le singleton
     */
    public static CompanyDAO getInstance(Connection connection) {
        if (companyDAO == null) {
            companyDAO = new CompanyDAO(connection);
        }
        return companyDAO;
    }

}
