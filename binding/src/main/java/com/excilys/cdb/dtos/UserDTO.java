package com.excilys.cdb.dtos;

public class UserDTO {
	private String username;
	private String password;
	
	public UserDTO() {
	}
	
	public UserDTO(String name, String password) {
		this.username = name;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", password=" + password + "]";
	}
	
	
	
	
}
