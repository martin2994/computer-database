package com.excilys.cdb.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * DAO pour Company Regroupe les différentes transactions sur les Companies.
 */

@Repository
public class CompanyDAO implements DAO<Company> {


    private final String ALL_COMPANIES = "FROM Company";
    
    private final String COMPANIES_BY_NAME = "FROM Company as company WHERE name LIKE :search ORDER BY name";

    private final String MAX_PAGE = "SELECT COUNT(id) FROM Company";
    
    private final String MAX_PAGE_BY_NAME = "SELECT COUNT(id) FROM Company WHERE name LIKE :search";
    
    private final String COMPUTERS_FOR_ONE_COMPANY = "FROM Computer WHERE company_id=:company_id ORDER BY name";
    
    private final String MAX_PAGE_BY_ID = "SELECT COUNT(id) FROM Computer WHERE company_id=:company_id";

    private final String DELETE_COMPANY = "DELETE FROM Company WHERE id = :id";

    private final String DELETE_COMPANY_COMPUTERS = "DELETE FROM Computer WHERE manufacturer.id = :id";

    private final String UPDATE_COMPANY = "UPDATE Company SET name=:name, logo=:logo WHERE id=:id";

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
        int nbElements = count();
        companies.setMaxPage(nbElements);
        companies.setNumberOfElements(nbElements);
        return companies;
    }
    
    /**
     * 
     * @param page
     * @param resultPerPage
     * @param search
     * @return
     * @throws Exception
     */
		public Page<Company> findPerPageByName(int page, int resultPerPage, String search) throws Exception {
    	 Page<Company> companies = new Page<>();
    	 String allSearch = "%" + search + "%";
       try (Session session = sessionFactory.getCurrentSession()) {
           session.beginTransaction();
           companies.setResults(session.createQuery(COMPANIES_BY_NAME, Company.class)
          		 .setFirstResult(page * resultPerPage)
               .setMaxResults(resultPerPage)
               .setParameter("search", allSearch)
               .getResultList());
           companies.setCurrentPage(page);
       } catch (IllegalArgumentException e) {
           throw new InvalidCompanyException(ExceptionMessage.BAD_ACCESS.getMessage());
       }
       int nbElements = countByName(search);
       companies.setMaxPage(nbElements);
       companies.setNumberOfElements(nbElements);
       return companies;
		}
		
		/**
     * Permet de récupérer la liste des computers avec un company_id spécifique.
     * @param id
     *            id à rechercher
     * @return la liste des computers
     * @throws InvalidComputerException
     *             Exception lancée quand la requete est mal formée
     */
    public List<Computer> getComputerByCompanyId(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            TypedQuery<Computer> typeQuery = session.createQuery(COMPUTERS_FOR_ONE_COMPANY, Computer.class);
            return typeQuery.setParameter("company_id", id).getResultList();
        }
    }
    
    /**
     * Permet de récupérer la liste des computers par page avec un company_id spécifique.
     * @param id
     *            id à rechercher
     * @param page
     *            la page à récupérer
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la liste des computers
     * @throws InvalidComputerException
     *             Exception lancée quand la requete est mal formée
     */
    public Page<Computer> getComputerByCompanyIdPerPage(long id, int page, int resultPerPage)
            throws InvalidComputerException {
        Page<Computer> computers = new Page<>();
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            TypedQuery<Computer> typeQuery = session.createQuery(COMPUTERS_FOR_ONE_COMPANY, Computer.class).setFirstResult(page * resultPerPage).setMaxResults(resultPerPage);
            typeQuery.setParameter("company_id", id);
            computers.setResults(typeQuery.getResultList());
            computers.setCurrentPage(page);
        } catch (IllegalArgumentException e) {
            throw new InvalidComputerException(ExceptionMessage.BAD_ACCESS.getMessage());
        }
        computers.setMaxPage(countById(id));
        return computers;
    }
    
    /**
     * Récupère le nombre de d'élement d'une recherche.
     * @param id
     *            id à rechercher
     * @return le nombre de computer
     */
    public int countById(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query query = session.createQuery(MAX_PAGE_BY_ID);
            query.setParameter("company_id", id);
            return (int) (long) query.getResultList().get(0);
        }
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
    
    /**
     * Récupère le nombre d'élement avec recherche.
     */
    public int countByName(String search) {
    	String allSearch = "%" + search + "%";
      try (Session session = sessionFactory.getCurrentSession()) {
          session.beginTransaction();
          Query query = session.createQuery(MAX_PAGE_BY_NAME);
          query.setParameter("search", allSearch);
          return (int) (long) query.getResultList().get(0);
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
            query.setParameter("logo", company.getLogo());
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
