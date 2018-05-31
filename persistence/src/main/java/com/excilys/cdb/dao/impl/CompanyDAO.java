package com.excilys.cdb.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

/**
 * DAO pour Company Regroupe les différentes transactions sur les Companies.
 */

@Repository
public class CompanyDAO implements DAO<Company> {


    private final String ALL_COMPANIES = "FROM Company";

    private final String MAX_PAGE = "SELECT COUNT(id) FROM Company";

    private final String DELETE_COMPANY = "DELETE FROM Company WHERE id = :id";

    private final String DELETE_COMPANY_COMPUTERS = "DELETE FROM Computer WHERE manufacturer.id = :id";

    private final String UPDATE_COMPANY = "UPDATE Company SET name=:name WHERE id=:id";

    private SessionFactory sessionFactory;

    /**
     * Constructeur privé qui injecte la dataSource.
     * @param sessionFactory
     *            la sessionFactory
     */
    private CompanyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Permet de récupérer la liste de toutes les companies.
     * @return La liste des Company
     */

    @Override
    public List<Company> findAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.createQuery(ALL_COMPANIES, Company.class).getResultList();
        }
    }

    /**
     * Permet de récupérer la liste de toutes les companies page par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            nombre de computer par page
     * @return La liste des Company
     * @throws InvalidCompanyException
     *             Exception lancée quand la requete echoue
     */
    @Override
    public Page<Company> findPerPage(int page, int resultPerPage) throws InvalidCompanyException {
        Page<Company> companies = new Page<>();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            companies.setResults(session.createQuery(ALL_COMPANIES, Company.class).setFirstResult(page * resultPerPage)
                    .setMaxResults(resultPerPage).getResultList());
            companies.setCurrentPage(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidCompanyException(ExceptionMessage.BAD_ACCESS.getMessage());
        }
        companies.setMaxPage(count());
        return companies;
    }

    /**
     * Récupère une company particulière.
     * @param id
     *            , l'id de la company à rechercher
     * @return La company correspondante
     */
    @Override
    public Optional<Company> findById(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return Optional.ofNullable(session.get(Company.class, id));
        }
    }

    /**
     * Récupère le nombre d'élement.
     */
    @Override
    public int count() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return (int) (long) session.createQuery(MAX_PAGE).getResultList().get(0);
        }
    }

    @Override
    public long add(Company company) throws NoObjectException {
        if (company == null) {
            String message = ExceptionMessage.NO_COMPANY_TO_CREATE.getMessage();
            throw new NoObjectException(message);
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return (long) session.save(company);
        }
    }

    @Override
    public boolean delete(long id) {
        boolean result = false;
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query query = session.createQuery(DELETE_COMPANY_COMPUTERS);
            query.setParameter("id", id);
            query.executeUpdate();
            query = session.createQuery(DELETE_COMPANY);
            query.setParameter("id", id);
            result = query.executeUpdate() == 1;
            sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
        return result;
    }

    @Override
    public Optional<Company> update(Company company) throws NoObjectException {
        Optional<Company> companyOpt = Optional.empty();
        int result = 0;
        if (company == null) {
            String message = ExceptionMessage.NO_COMPANY_TO_UPDATE.getMessage();
            throw new NoObjectException(message);
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query query = session.createQuery(UPDATE_COMPANY);
            query.setParameter("id", company.getId());
            query.setParameter("name", company.getName());
            result = query.executeUpdate();
            if (result > 0) {
                companyOpt = Optional.ofNullable(company);
            }
        }
        return companyOpt;
    }

    /**
     * Regarde si la company existe.
     * @param id
     *            la company à verifier
     * @return un booleen avec la réponse
     */
    @Override
    public boolean isExist(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.get(Company.class, id) != null;
        }
    }
}
