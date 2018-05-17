package com.excilys.cdb.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.SpringTestConfiguration;
import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

@SpringJUnitConfig(classes = SpringTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyDAO companyDAO;

    @Mock
    private ComputerDAO computerDAO;

    @InjectMocks
    private CompanyService companyService;

    @Autowired
    private CompanyService companyServiceBean;

    private Company company;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceTest.class);

    /**
     * Initialise les données Mockito et objet.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        company = new Company(1, "test");
    }

    /**
     * Réinitialise les données.
     */
    @AfterEach
    public void tearDown() {
        company = null;
    }

    /**
     * Teste le cas normal de la fonction GetCompanies.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompanies() throws SQLException {
        List<Company> companies = Collections.nCopies(5, new Company(1, "test"));
        Mockito.when(companyDAO.findAll()).thenReturn(companies);
        List<Company> companiesTest = companyService.getCompanies();
        LOGGER.info(companiesTest.toString());
        assertTrue(companiesTest.size() == 5 && companiesTest.get(0).equals(company));
        Mockito.verify(companyDAO).findAll();
    }

    /**
     * Teste le cas normal de la fonction GetCompanies avec page.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompaniesPerPage() throws SQLException {
        List<Company> companies = Collections.nCopies(5, company);
        Page<Company> page = new Page<>();
        page.setResults(companies);
        Mockito.when(companyDAO.findPerPage(1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(companyService.getCompanies(1, 10).getResults()));
        Mockito.verify(companyDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de companies par page est négatif.
     */
    @Test
    public void testGetCompaniesBadResultPerPage() {
        assertTrue(companyServiceBean.getCompanies(0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetCompanies quand la page est négative.
     */
    @Test
    public void testGetCompaniesWithBadPageInf() {
        Page<Company> companies = companyServiceBean.getCompanies(-1, 10);
        assertTrue(companies.getResults().isEmpty());
    }

    /**
     * Teste la fonction GetCompanies quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompaniesException() throws SQLException {
        Mockito.when(companyDAO.findPerPage(1, 10)).thenThrow(SQLException.class);
        assertTrue(companyService.getCompanies(1, 10).getResults().isEmpty());
        Mockito.verify(companyDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas normal de la fonction GetCompany.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testGetCompany() throws SQLException, InvalidCompanyException {
        Mockito.when(companyDAO.findById(1L)).thenReturn(Optional.ofNullable(company));
        assertTrue("test".equals(companyService.getCompany(1L).getName()));
        Mockito.verify(companyDAO).findById(1L);
    }

    /**
     * Teste la fonction GetCompany avec un id négatif.
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testGetCompanyWithBadId() {
        assertThrows(InvalidCompanyException.class, () -> companyServiceBean.getCompany(-1L));
    }

    /**
     * Teste la fonction GetCompany quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testGetCompanyException() throws SQLException, InvalidCompanyException {
        Mockito.when(companyDAO.findById(1L)).thenThrow(SQLException.class);
        assertThrows(InvalidCompanyException.class, () -> companyService.getCompany(1));
        Mockito.verify(companyDAO).findById(1L);
    }

    /**
     * Teste le cas normal de la fonction DeleteCompany.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompany() throws SQLException, InvalidCompanyException {
        Mockito.when(companyDAO.delete(1L)).thenReturn(true);
        assertTrue(companyService.deleteCompany(1L));
        Mockito.verify(companyDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteCompany quand il n'y a pas de correspodance.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompanyNoCompany() throws SQLException, InvalidCompanyException {
        Mockito.when(companyDAO.delete(1L)).thenReturn(false);
        assertFalse(companyService.deleteCompany(1L));
        Mockito.verify(companyDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteCompany quand l'id est négatif.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompanyBadIdInf() throws SQLException, InvalidCompanyException {
        assertThrows(InvalidCompanyException.class, () -> companyServiceBean.deleteCompany(-1L));
    }

    /**
     * Teste la fonction DeleteCompany quand la DAO lance une exception.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompanyException() throws SQLException, InvalidCompanyException {
        Mockito.when(companyDAO.delete(1L)).thenThrow(SQLException.class);
        assertFalse(companyService.deleteCompany(1L));
        Mockito.verify(companyDAO).delete(1L);

    }
}
