package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

public class ComputerDAOTest {

    private ComputerDAO computerDAO;

    private Computer computerTest;

    /**
     * Initialise un computer et la DAO avant chaque méthode.
     * @throws NoDAOException
     *             exception lancée pour la DAO
     * @throws NoFactoryException
     *             exception lancée pour la fabrique
     */
    @Before
    public void setUp() throws NoDAOException, NoFactoryException {
        computerTest = new Computer.Builder("test").build();
        computerDAO = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
    }

    /**
     * Met à null tous les objets.
     */
    @After
    public void tearDown() {
        computerDAO = null;
        computerTest = null;
    }

    /**
     * Test le cas normal de la fonction findById.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindById() throws SQLException {
        Computer computer = computerDAO.findById(1L);
        assertTrue(computer.getName().equals("MacBook"));
        assertTrue(computer.getId() == 1L);
        assertTrue(computer.getIntroduced() == null);
        assertTrue(computer.getDiscontinued() == null);
        assertTrue(computer.getManufacturer().getId() == 1L);
        assertTrue(computer.getManufacturer().getName().equals("Apple Inc."));
    }

    /**
     * Test le cas normal de la fonction FindAll.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindAll() throws SQLException {
        Page<Computer> page = computerDAO.findAll(1);
        assertTrue(page.getMaxPage() == 2);
        assertTrue(page.getResults().size() == 5);
    }

    /**
     * Test le cas normal de la fonction Add.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testAdd() throws SQLException {
        int id = computerDAO.add(computerTest);
        assertTrue(id == 13);
    }

    /**
     * Test la cas normal de la fonction Update.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testUpdate() throws SQLException {
        Computer computer = computerTest;
        computer.setId(5L);
        Computer computer2 = computerDAO.update(computer);
        assertTrue(computer2.equals(computer));
    }

    /**
     * Test le cas normal de la fonction Delete.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDelete() throws SQLException {
        computerDAO.delete(3L);
        Computer computer = computerDAO.findById(3L);
        assertTrue(computer == null);
    }

    /**
     * Test la fonction MaxPage.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testMaxPage() throws SQLException {
        int maxPage = computerDAO.getMaxPage();
        assertTrue(maxPage == 2);
    }

}
