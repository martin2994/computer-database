package com.excilys.cdb.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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

	public User findUserByUsername(String username) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();
			User user = session.get(User.class, username);
			return user;
		}
	}
}
