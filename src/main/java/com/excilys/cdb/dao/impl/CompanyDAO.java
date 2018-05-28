package com.excilys.cdb.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.dao.mapper.CompanyRowMapper;
import com.excilys.cdb.enums.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

/**
 * DAO pour Company Regroupe les différentes transactions sur les Companies.
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

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private MessageSource messageSource;

    /**
     * Constructeur privé qui injecte la dataSource.
     * @param dataSource la datasource
     * @param messageSource Messages internationalisés
     */
    @Autowired
    private CompanyDAO(DataSource dataSource, MessageSource messageSource) {
        this.dataSource = dataSource;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void initJdbc() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Permet de récupérer la liste de toutes les companies.
     * @return La liste des Company
     */

    @Override
    public List<Company> findAll() {
        return jdbcTemplate.query(ALL_COMPANIES, new CompanyRowMapper());
    }

    /**
     * Permet de récupérer la liste de toutes les companies page par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            nombre de computer par page
     * @return La liste des Company
     * @throws InvalidCompanyException
     *              Exception lancée quand la requete echoue
     */
    @Override
    public Page<Company> findPerPage(int page, int resultPerPage) throws InvalidCompanyException {
        Page<Company> companies = new Page<>();
        try {
            companies.setResults(jdbcTemplate.query(ALL_COMPANIES_PER_PAGE,
                    new Object[] {page * resultPerPage, resultPerPage }, new CompanyRowMapper()));
            companies.setCurrentPage(page);
            companies.setMaxPage(count());
        } catch (BadSqlGrammarException e) {
            String message = messageSource.getMessage(ExceptionMessage.BAD_ACCESS.getMessage(), null, LocaleContextHolder.getLocale());
            throw new InvalidCompanyException(message);
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
    public Optional<Company> findById(long id) throws NoObjectException {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(COMPANY_BY_ID, new Object[] {id }, new CompanyRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            String message = messageSource.getMessage(ExceptionMessage.NO_RESULT.getMessage(), null, LocaleContextHolder.getLocale());
            throw new NoObjectException(message);
        }
    }

    /**
     * Récupère le nombre d'élement.
     */
    @Override
    public int count() {
        return jdbcTemplate.queryForObject(MAX_PAGE, Integer.class);
    }

    @Override
    public long add(Company t) {
        return 0;
    }

    @Override
    public boolean delete(long id) {
        jdbcTemplate.update(DELETE_COMPANY_COMPUTERS, new Object[] {id });
        return jdbcTemplate.update(DELETE_COMPANY, new Object[] {id }) == 1;
    }

    @Override
    public Optional<Company> update(Company t) {
        return null;
    }

    /**
     * Regarde si la company existe.
     * @param id
     *            la company à verifier
     * @return un booleen avec la réponse
     * @throws NoObjectException
     *          Exception lancée quand il n'y a pas de resultat
     */
    @Override
    public boolean isExist(long id) throws NoObjectException {
        boolean result = false;
        try {
            result = jdbcTemplate.queryForObject(COMPANY_EXIST, new Object[] {id }, Integer.class) > 0;
        } catch (EmptyResultDataAccessException e) {
            String message = messageSource.getMessage(ExceptionMessage.NO_RESULT.getMessage(), null, LocaleContextHolder.getLocale());
            throw new NoObjectException(message);
        }
        return result;
    }

}
