package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

public class ComputerDAOTest {

    private ComputerDAO computerDAO;

    private Computer computerTest;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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
     * Teste le cas normal de la fonction findById.
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
     * Teste la fonction findById avec un mauvais id.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindByIdBadId() throws SQLException {
        Computer computer = computerDAO.findById(-1L);
        assertTrue(computer == null);
    }

    /**
     * Teste le cas normal de la fonction FindAll.
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
     * Teste la fonction FindAll avec une page au dessus des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindAllPageSup() throws SQLException {
        Page<Computer> page = computerDAO.findAll(100);
        assertTrue(page.getResults().size() == 0);
    }

    /**
     * Teste la fonction FindAll avec une page au dessosu des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindAllPageInf() throws SQLException {
        Page<Computer> page = computerDAO.findAll(-1);
        assertTrue(page == null);
    }

    /**
     * Teste le cas normal de la fonction Add.
     * @throws SQLException
     *             exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAdd() throws SQLException, NoObjectException {
        int id = computerDAO.add(computerTest);
        assertTrue(id == 13);
    }

    /**
     * Teste la fonction Add avec un argument null.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet null
     */
    @Test
    public void testAddNull() throws SQLException, NoObjectException {
        exception.expect(NoObjectException.class);
        computerDAO.add(null);

    }

    /**
     * Teste la fonction Add quand on ajoute une company inexistante.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null
     */
    @Test
    public void testAddWithInexistantCompany() throws SQLException, NoObjectException {
        Company company = new Company();
        company.setId(60);
        company.setName("test");
        computerTest.setManufacturer(company);
        exception.expect(NoObjectException.class);
        computerDAO.add(computerTest);
    }

    /**
     * Teste la fonction Add avec un argument possédent un id.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAddWithId() throws SQLException, NoObjectException {
        computerTest.setId(1L);
        int id = computerDAO.add(computerTest);
        assertTrue(id == 14);
    }

    /**
     * Teste la cas normal de la fonction Update.
     * @throws SQLException
     *             exception SQL lancée
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdate() throws SQLException, NoObjectException {
        Computer computer = computerTest;
        computer.setId(5L);
        Computer computer2 = computerDAO.update(computer);
        assertTrue(computer2.equals(computer));
    }

    /**
     * Teste la fonction Update quand le computer est null.
     * @throws SQLException
     *             exception SQL lancée
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateNull() throws SQLException, NoObjectException {
        exception.expect(NoObjectException.class);
        computerDAO.update(null);
    }

    /**
     * Teste le cas normal de la fonction Delete.
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
     * Teste la fonction Delete quand l'objet est inexistant.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDeleteNoComputer() throws SQLException {
        computerDAO.delete(3L);
        Computer computer = computerDAO.findById(3L);
        assertTrue(computer == null);
    }

    /**
     * Teste la fonction MaxPage.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testMaxPage() throws SQLException {
        int maxPage = computerDAO.getMaxPage();
        assertTrue(maxPage == 2);
    }

}
