package com.excilys.cdb.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User.UserBuilder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.impl.UserDAO;
import com.excilys.cdb.exceptions.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.model.User;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	private UserDAO userDAO;

	public UserDetailsServiceImp(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findUserByUsername(username);
		UserBuilder builder = null;
		if (user != null) {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.disabled(!user.isEnabled());
			builder.password(user.getPassword());
			String[] authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new);
			builder.authorities(authorities);
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		return builder.build();
	}

	public void addUser(User user) throws NoObjectException {
		if (user != null && !StringUtils.isBlank(user.getUsername()) && !StringUtils.isBlank(user.getPassword())) {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			userDAO.add(user);
		}else {
			throw new NoObjectException(ExceptionMessage.INVALID_INFO.getMessage());
		}
	}

}
