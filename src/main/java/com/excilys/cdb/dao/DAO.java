package main.java.com.excilys.cdb.dao;

import java.sql.SQLException;
import java.util.List;

import main.java.com.excilys.cdb.utils.Page;

/**
 * Interface regroupant toutes les transactions sur la BD
 * 
 * @param <T> L'objet manipulé
 */
public interface DAO<T> {
	  Page<T> findAll(int page) throws SQLException;
	  T findById(int id) throws SQLException;
	  void add(T t) throws SQLException;
	  void delete(int id) throws SQLException;
	  void update(T t) throws SQLException;
	  int getMaxPage() throws SQLException;
}
