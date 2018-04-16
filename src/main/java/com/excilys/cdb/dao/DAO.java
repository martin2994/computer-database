package main.java.com.excilys.cdb.dao;

import java.sql.SQLException;
import java.util.List;


public interface DAO<T> {
	  List<T> findAll() throws SQLException;
	  T findOneById(int id) throws SQLException;
	  void add(T t) throws SQLException;
	  void delete(int id);
	  void update(T t);
}
