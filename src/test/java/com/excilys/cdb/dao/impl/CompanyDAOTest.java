package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.SpringTestConfiguration;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

@SpringJUnitConfig(classes = SpringTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class CompanyDAOTest {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ComputerDAO computerDAO;

    // @Rule
    // public final ExpectedException exception = ExpectedException.none();

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAOTest.class);

    /**
     * Teste le cas normal de la fonction findById.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindById() throws SQLException {
        Company company = companyDAO.findById(1L).get();
        assertTrue("Apple Inc.".equals(company.getName()));
        assertTrue(company.getId() == 1L);
    }

    /**
     * Teste la fonction findById avec un mauvais id.
     * @throws SQLException
     *             exception SQL lancée
     */
    // @Test
    // public void testFindByIdBadId() throws SQLException {
    // exception.expect(NoSuchElementException.class);
    // companyDAO.findById(-1L).get();
    // }

    /**
     * Teste le cas normal de la fonction FindAll.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testFindAll() throws SQLException {
        List<Company> list = companyDAO.findAll();
        assertTrue(list.size() == 2);
    }

    /**
     * Teste le cas normal de la fonction FindPerPage.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindPerPage() throws SQLException {
        Page<Company> page = companyDAO.findPerPage(0, 10);
        assertTrue(page.getMaxPage() == 1);
        assertTrue(page.getResults().size() == 2);
    }

    /**
     * Teste la fonction FindPerPage quand le nombre de company par page est
     * négatif.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testFindPerPageBadResultPerPage() throws SQLException {
        Page<Company> page = companyDAO.findPerPage(0, -1);
        assertTrue(page.getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessus des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindPerPagePageSup() throws SQLException {
        Page<Company> page = companyDAO.findPerPage(100, 10);
        assertTrue(page.getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessosu des limites.
     * @throws SQLException
     *             SQLException Exception SQL lancée
     */
    @Test
    public void testFindPerPagePageInf() throws SQLException {
        Page<Company> page = companyDAO.findPerPage(-1, 10);
        assertTrue(page.getResults().isEmpty());
    }

    /**
     * Teste le cas normal de la fonction Delete.
     * @throws NoFactoryException
     *             Exception lancée si la factory n'existe pas
     * @throws NoDAOException
     *             Exception lancée si une des DAO n'existe pas
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDelete() throws SQLException, NoDAOException, NoFactoryException {
        assertTrue(companyDAO.delete(2L));
        assertTrue(Optional.empty().equals(computerDAO.findById(2L)));
        assertTrue(Optional.empty().equals(companyDAO.findById(2L)));
    }

    /**
     * Teste la fonction Delete quand l'objet est inexistant.
     * @throws SQLException
     *             exception SQL lancée
     */
    @Test
    public void testDeleteNoComputer() throws SQLException {
        assertFalse(companyDAO.delete(100L));
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
