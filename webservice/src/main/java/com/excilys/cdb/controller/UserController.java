package com.excilys.cdb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dtos.UserDTO;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.mapper.DTOMapper;
import com.excilys.cdb.model.User;
import com.excilys.cdb.services.UserDetailsServiceImp;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private UserDetailsServiceImp userDetailsServiceImp;

	public UserController(UserDetailsServiceImp userDetailsServiceImp) {
		this.userDetailsServiceImp = userDetailsServiceImp;
	}

	@PostMapping
	public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO)
			throws InvalidComputerException, NoObjectException {
		User user = DTOMapper.toUser(userDTO);
		userDetailsServiceImp.addUser(user);
		return new ResponseEntity<> (HttpStatus.CREATED);
	}

}
