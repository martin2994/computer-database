package com.excilys.cdb.dao.impl;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.User;

@Repository
public class UserDAO {

	private SessionFactory sessionFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
	
	/**
	 * Constructeur privé qui injecte la dataSource.
	 * 
	 * @param sessionFactory
	 *            la sessionFactory
	 */
	private UserDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Optional<User> findUserByUsername(String username) {
		Optional<User> user = Optional.empty();
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			user = Optional.ofNullable(session.get(User.class, username));
			return user;
		}
	}
	
	public void add(User user) throws NoObjectException {
		if (user == null) {
			String message = ExceptionMessage.INVALID_ID.getMessage();
			throw new NoObjectException(message);
		}
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			session.save(user);
			session.save(user.getAuthorities().get(0));
			session.flush();
			session.close();
		} catch (DataException e) {
			throw new NoObjectException(ExceptionMessage.INVALID_INFO.getMessage());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
}
