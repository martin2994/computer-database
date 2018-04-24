package com.excilys.cdb.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;

public class DAOFactoryTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Test le cas normal de la fonction GetDAO pour Company.
     * @throws NoDAOException
     *             Exception lancée pour la DAO
     * @throws NoFactoryException
     *             Exception lancée pour la fabrique
     */
    @Test
    public void testGetCompanyDAO() throws NoDAOException, NoFactoryException {
        CompanyDAO dao = (CompanyDAO) DAOFactory.getDAO(DAOType.COMPANY);
        assertTrue(dao instanceof CompanyDAO);
        assertNotNull(dao);
    }

    /**
     * Test le cas normal de la fonction GetDAO pour Computer.
     * @throws NoDAOException
     *             Exception lancée pour la DAO
     * @throws NoFactoryException
     *             Exception lancée pour la fabrique
     */
    @Test
    public void testGetComputerDAO() throws NoDAOException, NoFactoryException {
        ComputerDAO dao = (ComputerDAO) DAOFactory.getDAO(DAOType.COMPUTER);
        assertTrue(dao instanceof ComputerDAO);
        assertNotNull(dao);
    }

    /**
     * Test l'exception DAOException.
     * @throws NoDAOException
     *             Exception lancée pour la DAO
     * @throws NoFactoryException
     *             Exception lancée pour la fabrique
     */
    @Test
    public void testDAOException() throws NoDAOException, NoFactoryException {
        exception.expect(NoDAOException.class);
        DAOFactory.getDAO(null);
    }
}
