package com.excilys.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

/**
 * DAO pour Company Regroupe les différentes transactions sur les Company.
 */

@Repository
public class CompanyDAO implements DAO<Company> {

    /**
     * Requete pour le findAll.
     */
    private final String ALL_COMPANIES = "SELECT id,name FROM company";

    /**
     * Requete pour le findPerPage.
     */
    private final String ALL_COMPANIES_PER_PAGE = "SELECT id,name FROM company LIMIT ?,?";

    /**
     * Requete pour le findById.
     */
    private final String COMPANY_BY_ID = "SELECT id,name FROM company WHERE id=?";

    /**
     * Requete pour le nombre de page.
     */
    private final String MAX_PAGE = "SELECT COUNT(id) FROM company";

    /**
     * Requete pour voir l'existance d'une company.
     */
    private final String COMPANY_EXIST = "SELECT company.id FROM company WHERE company.id = ?";

    /**
     * Requete pour le delete.
     */
    private final String DELETE_COMPANY = "DELETE FROM company WHERE company.id = ?";

    /**
     * Requete pour le delete les computers liés.
     */
    private final String DELETE_COMPANY_COMPUTERS = "DELETE FROM computer WHERE computer.company_id = ?";

    @Autowired
    private DataSource dataSource;

    /**
     * Constructeur privé vide.
     */
    private CompanyDAO() {
    }

    /**
     * Permet de récupérer la liste de toutes les company.
     * @return La liste des Company
     * @throws SQLException
     *             Exception SQL lancée
     */


    @Override
    public List<Company> findAll() throws SQLException {
        List<Company> companies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(ALL_COMPANIES);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                companies.add(new Company(rs.getInt("id"), rs.getString("name")));
            }
        }
        return companies;

    }

    /**
     * Permet de récupérer la liste de toutes les company page par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            nombre de computer par page
     * @return La liste des Company
     */
    @Override
    public Page<Company> findPerPage(int page, int resultPerPage) throws SQLException {
        Page<Company> companies = new Page<>();
        if (page >= 0 && resultPerPage >= 1) {
            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(ALL_COMPANIES_PER_PAGE)) {
                companies.setResultPerPage(resultPerPage);
                statement.setInt(1, page * resultPerPage);
                statement.setInt(2, resultPerPage);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        companies.add(new Company(rs.getInt("id"), rs.getString("name")));
                    }
                }
            }
            companies.setCurrentPage(page);
            companies.setMaxPage(count());
        }
        return companies;
    }

    /**
     * Récupère une company particulière.
     * @param id
     *            , l'id de la company à rechercher
     * @return La company correspondante
     */
    @Override
    public Optional<Company> findById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(COMPANY_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                Optional<Company> company = Optional.empty();
                if (rs.next()) {
                    company = Optional.ofNullable(new Company(rs.getInt("id"), rs.getString("name")));
                }
                return company;
            }
        }
    }

    /**
     * Récupère le nombre d'élement.
     */
    @Override
    public int count() throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(MAX_PAGE);
                ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }

    }

    @Override
    public long add(Company t) {
        return 0;
    }

    @Override
    public boolean delete(long id) throws SQLException {
        int result = 0;
        boolean delete = false;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statementComputer = connection.prepareStatement(DELETE_COMPANY_COMPUTERS);
                    PreparedStatement statementCompany = connection.prepareStatement(DELETE_COMPANY)) {
                statementComputer.setLong(1, id);
                statementComputer.executeUpdate();
                statementCompany.setLong(1, id);
                result = statementCompany.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
            connection.commit();
            if (result != 0) {
                delete = true;
            }
            return delete;
        }
    }

    @Override
    public Optional<Company> update(Company t) {
        return null;
    }

    /**
     * Regarde si la company.
     * @param id
     *            la company à verifier
     * @return un booleen avec la réponse
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Override
    public boolean isExist(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(COMPANY_EXIST, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        }
    }

}
