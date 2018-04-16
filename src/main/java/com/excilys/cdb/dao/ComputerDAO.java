package main.java.com.excilys.cdb.dao;

import java.util.List;

import main.java.com.excilys.cdb.model.Computer;;

public interface ComputerDAO {
	 List<Computer> findAll();
	 Computer findOneById(int id);
	 void add(Computer computer);
	 void delete(int id);
	 void update(Computer computer);
}
