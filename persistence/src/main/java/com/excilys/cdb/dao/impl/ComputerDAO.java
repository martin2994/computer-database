package com.excilys.cdb.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * DAO des Computer Regroupe l'ensemble des transactions sur les computers.
 */
@Repository
public class ComputerDAO implements DAO<Computer> {

	private final String ALL_COMPUTERS = "FROM Computer";

	private final String COMPUTERS_BY_NAME = "FROM Computer as computer WHERE name LIKE :search OR manufacturer.name LIKE :search ORDER BY name";

	private final String UPDATE_COMPUTER = "UPDATE Computer SET name=:name, introduced=:introduced, discontinued=:discontinued, manufacturer=:company WHERE id=:id";

	private final String DELETE_COMPUTER = "DELETE FROM Computer WHERE id = :id";

	private final String DELETE_COMPUTER_LIST = "DELETE FROM Computer WHERE id IN (:listId)";

	private final String MAX_PAGE = "SELECT COUNT(id) FROM Computer";

	private final String MAX_PAGE_BY_NAME = "SELECT COUNT(id) FROM Computer WHERE name LIKE :search or manufacturer.name LIKE :search ";

	private SessionFactory sessionFactory;

	/**
	 * Constructeur privé qui injecte la dataSource.
	 * 
	 * @param sessionFactory
	 *            la sessionFactory
	 */
	private ComputerDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Récupère la liste de tous les computers.
	 * 
	 * @return la liste des computers
	 */
	@Override
	public List<Computer> findAll() {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			return session.createQuery(ALL_COMPUTERS, Computer.class).getResultList();
		}
	}

	/**
	 * Permet de récupérer la liste de tous les computers page par page.
	 * 
	 * @param page
	 *            la page à afficher
	 * @param resultPerPage
	 *            nombre de company par page
	 * @return La liste des computer
	 */
	@Override
	public Page<Computer> findPerPage(int page, int resultPerPage) throws InvalidComputerException {
		Page<Computer> computers = new Page<>();
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			TypedQuery<Computer> typedQuery = session.createQuery(ALL_COMPUTERS, Computer.class)
					.setFirstResult(page * resultPerPage).setMaxResults(resultPerPage);
			computers.setResults(typedQuery.getResultList());
			computers.setCurrentPage(page);
		} catch (IllegalArgumentException e) {
			throw new InvalidComputerException(ExceptionMessage.BAD_ACCESS.getMessage());
		}
		computers.setMaxPage(count());
		return computers;
	}

	/**
	 * Permet de récupérer la liste des computers par page avec un nom spécifique.
	 * 
	 * @param search
	 *            le nom à rechercher
	 * @param page
	 *            la page à récupérer
	 * @param resultPerPage
	 *            le nombre d'élément par page
	 * @return la liste des computers
	 * @throws InvalidComputerException
	 *             Exception lancée quand la requete est mal formée
	 */
	public Page<Computer> findByNamePerPage(String search, int page, int resultPerPage)
			throws InvalidComputerException {
		Page<Computer> computers = new Page<>();
		String allSearch = "%" + search + "%";
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			TypedQuery<Computer> typeQuery = session.createQuery(COMPUTERS_BY_NAME, Computer.class)
					.setFirstResult(page * resultPerPage).setMaxResults(resultPerPage);
			typeQuery.setParameter("search", allSearch);
			computers.setResults(typeQuery.getResultList());
			computers.setCurrentPage(page);
		} catch (IllegalArgumentException e) {
			throw new InvalidComputerException(ExceptionMessage.BAD_ACCESS.getMessage());
		}
		computers.setMaxPage(countByName(search));
		return computers;
	}

	/**
	 * Récupère un computer particulier.
	 * 
	 * @param id
	 *            l'id du computer à rechercher
	 * @return Le computer correspondant
	 * @throws NoObjectException
	 *             Excpetion lancée si la requete échoue
	 */
	@Override
	public Optional<Computer> findById(long id) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			return Optional.ofNullable(session.get(Computer.class, id));
		}
	}

	/**
	 * Ajoute un computer dans la base.
	 * 
	 * @param computer
	 *            les informations du computer à ajouter
	 * @throws NoObjectException
	 *             Exception lancée quand un objet est null ou inexistant
	 * @throws InvalidComputerException
	 *
	 */
	@Override
	public long add(Computer computer) throws NoObjectException, InvalidComputerException {
		if (computer == null) {
			String message = ExceptionMessage.NO_COMPUTER_TO_CREATE.getMessage();
			throw new NoObjectException(message);
		}
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			return (long) session.save(computer);
		} catch (DataException e) {
			throw new InvalidComputerException(ExceptionMessage.INVALID_INFO.getMessage());
		}
	}

	/**
	 * Regarde si le computer existe.
	 * 
	 * @param id
	 *            le computer à verifier
	 * @return un booleen avec la réponse
	 * @throws NoObjectException
	 *             Exception lancée quand la requete echoue (pas de resultat)
	 */
	@Override
	public boolean isExist(long id) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			return session.get(Computer.class, id) != null;
		}
	}

	/**
	 * Supprime un computer de la base.
	 * 
	 * @param id
	 *            l'id du computer à supprimer
	 * @return Si la suppression a été effectuée
	 */
	@Override
	public boolean delete(long id) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			Query query = session.createQuery(DELETE_COMPUTER);
			query.setParameter("id", id);
			return query.executeUpdate() == 1;
		}
	}

	/**
	 * Permet de supprimer une liste d'id sous forme de string.
	 * 
	 * @param idList
	 *            la liste d'id
	 * @return un boolean pour savoir si la suppression a eu lieu ou non
	 */
	public boolean deleteList(Set<Long> idList) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			Query query = session.createQuery(DELETE_COMPUTER_LIST);
			query.setParameter("listId", idList);
			return query.executeUpdate() > 0;
		}
	}

	/**
	 * Modifie un computer avec les informations données.
	 * 
	 * @param computer
	 *            les informations du computer à modifier
	 * @throws NoObjectException
	 *             Exception lancée quand un objet est null ou inexistant
	 * @throws InvalidComputerException
	 * @throws SQLException
	 *             Exception SQL lancée
	 */
	@Override
	public Optional<Computer> update(Computer computer) throws NoObjectException, InvalidComputerException {
		Optional<Computer> computerOpt = Optional.empty();
		int result = 0;
		if (computer == null) {
			String message = ExceptionMessage.NO_COMPUTER_TO_UPDATE.getMessage();
			throw new NoObjectException(message);
		}
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			Query query = session.createQuery(UPDATE_COMPUTER);
			query.setParameter("id", computer.getId());
			query.setParameter("name", computer.getName());
			query.setParameter("introduced", computer.getIntroduced());
			query.setParameter("discontinued", computer.getDiscontinued());
			query.setParameter("company", computer.getManufacturer());
			result = query.executeUpdate();
			if (result > 0) {
				computerOpt = Optional.ofNullable(computer);
			}
		} catch (DataException e) {
			throw new InvalidComputerException(ExceptionMessage.INVALID_INFO.getMessage());
		}
		return computerOpt;
	}

	/**
	 * Récupère le nombre de d'élement total.
	 */
	@Override
	public int count() {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			return (int) (long) session.createQuery(MAX_PAGE).getResultList().get(0);
		}
	}

	/**
	 * Récupère le nombre de d'élement d'une recherche.
	 * 
	 * @param search
	 *            le nom à rechercher
	 * @return le nombre de computer
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

}
