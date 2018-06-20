package com.excilys.cdb.services;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.SpringTestConfigurationService;
import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfigurationService.class)
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

    @Rule
    public final ExpectedException exception = ExpectedException.none();
   

    /**
     * Initialise les données Mockito et objet.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        company = new Company(1, "test");
    }

    /**
     * Réinitialise les données.
     */
    @After
    public void tearDown() {
        company = null;
    }

    /**
     * Teste le cas normal de la fonction GetCompanies.
     */
    @Test
    public void testGetCompanies() {
        List<Company> companies = Collections.nCopies(5, new Company(1, "test"));
        Mockito.when(companyDAO.findAll()).thenReturn(companies);
        List<Company> companiesTest = companyService.getCompanies();
        assertTrue(companiesTest.size() == 5 && companiesTest.get(0).equals(company));
        Mockito.verify(companyDAO).findAll();
    }

    /**
     * Teste le cas normal de la fonction GetCompanies avec page.
     * @throws InvalidCompanyException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetCompaniesPerPage() throws InvalidCompanyException {
        List<Company> companies = Collections.nCopies(5, company);
        Page<Company> page = new Page<>();
        page.setResults(companies);
        Mockito.when(companyDAO.findPerPage(1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(companyService.getCompanies(1, 10).getResults()));
        Mockito.verify(companyDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de companies par page est négatif.
     * @throws InvalidCompanyException
     *                  Exception lancée quand la requete echoue
     */
    @Test
    public void testGetCompaniesBadResultPerPage() throws InvalidCompanyException {
        assertTrue(companyServiceBean.getCompanies(0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetCompanies quand la page est négative.
     * @throws InvalidCompanyException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetCompaniesWithBadPageInf() throws InvalidCompanyException {
        Page<Company> companies = companyServiceBean.getCompanies(-1, 10);
        assertTrue(companies.getResults().isEmpty());
    }

    /**
     * Teste le cas normal de la fonction GetCompany.
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     * @throws NoObjectException
     *              Exception lancée quand la requete echoue (pas de resultat)
     * @throws InvalidIdException 
     */
    @Test
    public void testGetCompany() throws InvalidCompanyException, NoObjectException, InvalidIdException {
        Mockito.when(companyDAO.findById(1L)).thenReturn(Optional.ofNullable(company));
        assertTrue("test".equals(companyService.getCompany(1L).getName()));
        Mockito.verify(companyDAO).findById(1L);
    }

    /**
     * Teste la fonction GetCompany avec un id négatif.
     * @throws NoObjectException 
     * @throws InvalidCompanyException
     *             Exception lancée quand la company n'est pas valide
     * @throws InvalidIdException 
     */
    @Test
    public void testGetCompanyWithBadId() throws InvalidCompanyException, NoObjectException, InvalidIdException {
    	exception.expect(InvalidCompanyException.class);
        companyServiceBean.getCompany(-1L);
    }

    /**
     * Teste le cas normal de la fonction DeleteCompany.
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompany() throws InvalidCompanyException {
        Mockito.when(companyDAO.delete(1L)).thenReturn(true);
        assertTrue(companyService.deleteCompany(1L));
        Mockito.verify(companyDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteCompany quand il n'y a pas de correspodance.
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompanyNoCompany() throws InvalidCompanyException {
        Mockito.when(companyDAO.delete(1L)).thenReturn(false);
        assertFalse(companyService.deleteCompany(1L));
        Mockito.verify(companyDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteCompany quand l'id est négatif.
     * @throws InvalidCompanyException
     *             Exception lancée quand l'id n'est pas valide
     */
    @Test
    public void testDeleteCompanyBadIdInf() throws InvalidCompanyException {
    	exception.expect(InvalidCompanyException.class);
        companyServiceBean.deleteCompany(-1L);
    }

}
