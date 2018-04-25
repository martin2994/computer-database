package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

public class CompanyDAOTest {

    private CompanyDAO companyDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initialise une company et la DAO avant chaque méthode.
     * @throws NoDAOException
     *             exception lancée pour la DAO
     * @throws NoFactoryException
     *             exception lancée pour la fabrique
     */
    @Before
    public void setUp() throws NoDAOException, NoFactoryException {
        companyDAO = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
    }

    /**
     * Met à null tous les objets.
     */
    @After
    public void tearDown() {
        companyDAO = null;
    }

    /**
     * Teste le cas normal de la fonction findById.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindById() throws SQLException {
        Company company = companyDAO.findById(1L);
        assertTrue(company.getName().equals("Apple Inc."));
        assertTrue(company.getId() == 1L);
    }

    /**
     * Teste la fonction findById avec un mauvais id.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindByIdBadId() throws SQLException {
        Company company = companyDAO.findById(-1L);
        assertNull(company);
    }

    /**
     * Teste le cas normal de la fonction FindAll.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindAll() throws SQLException {
        Page<Company> page = companyDAO.findAll(0);
        assertTrue(page.getMaxPage() == 0);
        assertTrue(page.getResults().size() == 2);
    }

    /**
     * Teste la fonction FindAll avec une page au dessus des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindAllPageSup() throws SQLException {
        Page<Company> page = companyDAO.findAll(100);
        assertTrue(page.getResults().size() == 0);
    }

    /**
     * Teste la fonction FindAll avec une page au dessosu des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindAllPageInf() throws SQLException {
        Page<Company> page = companyDAO.findAll(-1);
        assertNull(page);
    }

    /**
     * Teste la fonction Count.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testCount() throws SQLException {
        int maxPage = companyDAO.count();
        assertTrue(maxPage == 2);
    }

    /**
     * Teste la fonction isExist.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExist() throws SQLException {
        boolean test = companyDAO.isExist(1L);
        assertTrue(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExistWithBadId() throws SQLException {
        boolean test = companyDAO.isExist(100L);
        assertFalse(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     * @throws SQLException
     *             ExceptionSQL lancée
     */
    @Test
    public void testIsExistBadArgument() throws SQLException {
        boolean test = companyDAO.isExist(-1L);
        assertFalse(test);
    }
}
