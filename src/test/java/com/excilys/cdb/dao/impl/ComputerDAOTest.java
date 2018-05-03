package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.services.Facade;
import com.excilys.cdb.utils.Page;

public class ComputerDAOTest {

    private ComputerDAO computerDAO;

    private Computer computerTest;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

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
     * Teste le cas normal de la fonction FindAll.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testFindAll() throws SQLException {
        List<Computer> list = computerDAO.findAll();
        assertTrue(list.size() == 15);
    }

    /**
     * Teste le cas normal de la fonction findById.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindById() throws SQLException {
        Computer computer = computerDAO.findById(1L).get();
        assertTrue("MacBook".equals(computer.getName()));
        assertTrue(computer.getId() == 1L);
        assertNull(computer.getIntroduced());
        assertNull(computer.getDiscontinued());
        assertTrue(computer.getManufacturer().getId() == 1L);
        assertTrue("Apple Inc.".equals(computer.getManufacturer().getName()));
    }

    /**
     * Teste la fonction findById avec un mauvais id.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindByIdBadId() throws SQLException {
        exception.expect(NoSuchElementException.class);
        computerDAO.findById(-1L).get();
    }

    /**
     * Teste le cas normal de la fonction FindPerPage.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindPerPage() throws SQLException {
        Page<Computer> page = computerDAO.findPerPage(0, 10);
        assertTrue(page.getMaxPage() == 2);
        assertTrue(page.getResults().size() == 10);
    }

    /**
     * Teste la fonction FindPerPage quand le nombre de computer par page est
     * négatif.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindPerPageBadResultPerPage() throws SQLException {
        Page<Computer> page = computerDAO.findPerPage(0, -1);
        assertTrue(page.getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessus des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindPerPagePageSup() throws SQLException {
        Page<Computer> page = computerDAO.findPerPage(100, 10);
        assertTrue(page.getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessosu des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindPerPagePageInf() throws SQLException {
        Page<Computer> page = computerDAO.findPerPage(-1, 10);
        assertTrue(page.getResults().isEmpty());
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
        long id = computerDAO.add(computerTest);
        assertTrue(id == 15);
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
        try {
            computerDAO.add(computerTest);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
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
        long id = computerDAO.add(computerTest);
        assertTrue(id == 16);
    }

    /**
     * Teste la fonction Add avec des dates.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAddDate() throws SQLException, NoObjectException {
        computerTest.setIntroduced(LocalDate.parse("2010-01-01"));
        long id = computerDAO.add(computerTest);
        Computer computer = computerDAO.findById(id).get();
        assertTrue(computer.getIntroduced().equals(LocalDate.parse("2010-01-01")));
        assertNull(computer.getDiscontinued());
        computerTest.setDiscontinued(LocalDate.parse("2011-01-01"));
        id = computerDAO.add(computerTest);
        computer = computerDAO.findById(id).get();
        assertTrue(computer.getDiscontinued().equals(LocalDate.parse("2011-01-01")));
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
        Company company = new Company();
        company.setId(1L);
        computer.setManufacturer(company);
        Computer computer2 = computerDAO.update(computer).get();
        assertTrue(computer2.equals(computer));
    }

    /**
     * Teste la fonction Update sur unr computer avec un id inexistant.
     * @throws SQLException
     *             exception SQL lancée
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateWithInexistantId() throws SQLException, NoObjectException {
        Computer computer = computerTest;
        computer.setId(700L);
        exception.expect(NoSuchElementException.class);
        computerDAO.update(computer).get();
    }

    /**
     * Teste la fonction update quand on change avec une company inexistante.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null
     */
    @Test
    public void testUpdateWithInexistantCompany() throws SQLException, NoObjectException {
        Company company = new Company();
        company.setId(60);
        company.setName("test");
        computerTest.setManufacturer(company);
        computerTest.setId(1);
        try {
            computerDAO.add(computerTest);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
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
     * Teste la fonction Update avec des dates.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateDate() throws SQLException, NoObjectException {
        computerTest.setId(4L);
        computerTest.setIntroduced(LocalDate.parse("2010-01-01"));
        Computer computer = computerDAO.update(computerTest).get();
        assertTrue(computer.getIntroduced().equals(LocalDate.parse("2010-01-01")));
        assertNull(computer.getDiscontinued());
        computerTest.setDiscontinued(LocalDate.parse("2011-01-01"));
        computer = computerDAO.update(computerTest).get();
        assertTrue(computer.getDiscontinued().equals(LocalDate.parse("2011-01-01")));
    }

    /**
     * Teste le cas normal de la fonction Delete.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDelete() throws SQLException {
        assertTrue(computerDAO.delete(3L));
        exception.expect(NoSuchElementException.class);
        computerDAO.findById(3L).get();
    }

    /**
     * Teste la fonction Delete quand l'objet est inexistant.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDeleteNoComputer() throws SQLException {
        assertFalse(computerDAO.delete(100L));
    }

    /**
     * Teste la fonction Count.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testCount() throws SQLException {
        int maxPage = computerDAO.count();
        assertTrue(maxPage == 15);
    }

    /**
     * Teste la fonction isExist.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExist() throws SQLException {
        boolean test = computerDAO.isExist(1L);
        assertTrue(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExistWithBadId() throws SQLException {
        boolean test = computerDAO.isExist(100L);
        assertFalse(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExistBadArgument() throws SQLException {
        boolean test = computerDAO.isExist(-1L);
        assertFalse(test);
    }

}
