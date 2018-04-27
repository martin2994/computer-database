package com.excilys.cdb.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

@RunWith(MockitoJUnitRunner.class)
public class FacadeTest {

    @Mock
    private CompanyDAO companyDAO;

    @Mock
    private ComputerDAO computerDAO;

    @InjectMocks
    private Facade facade;

    private Computer computer;

    private Company company;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initialise les données Mockito et objet.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        computer = new Computer.Builder("test").build();
        company = new Company(1, "test");
    }

    /**
     * Réinitialise les données.
     */
    @After
    public void tearDown() {
        computer = null;
        company = null;
    }

    /**
     * Teste le cas normal de la fonction GetComputers.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputers() throws SQLException {
        List<Computer> computers = Collections.nCopies(5, computer);
        Page<Computer> page = new Page<>();
        page.setResults(computers);
        Mockito.when(computerDAO.findPerPage(1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(facade.getComputers(1, 10).getResults()));
        Mockito.verify(computerDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de computers par page est négatif.
     */
    @Test
    public void testGetComputersBadResultPerPage() {
        assertNull(facade.getComputers(0, -1));
    }

    /**
     * Teste la fonction GetComputers quand la page est négative.
     */
    @Test
    public void testGetComputersWithBadPageInf() {
        Page<Computer> computers = facade.getComputers(-1, 10);
        assertNull(computers);
    }

    /**
     * Teste l'exception de GetComputerException.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputersException() throws SQLException {
        Mockito.when(computerDAO.findPerPage(1, 10)).thenThrow(SQLException.class);
        assertNull(facade.getComputers(1, 10));
        Mockito.verify(computerDAO).findPerPage(1, 10);
    }


    /**
     * Teste le cas normal de la fonction GetCompanies.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompanies() throws SQLException {
        List<Company> companies = Collections.nCopies(5, company);
        Mockito.when(companyDAO.findAll()).thenReturn(companies);
        List<Company> companiesTest = facade.getCompanies();
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
        assertTrue(page.getResults().equals(facade.getCompanies(1, 10).getResults()));
        Mockito.verify(companyDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de companies par page est négatif.
     */
    @Test
    public void testGetCompaniesBadResultPerPage() {
        assertNull(facade.getCompanies(0, -1));
    }

    /**
     * Teste la fonction GetCompanies quand la page est négative.
     */
    @Test
    public void testGetCompaniesWithBadPageInf() {
        Page<Company> companies = facade.getCompanies(-1, 10);
        assertNull(companies);
    }

    /**
     * Teste la fonction GetCompanies quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompaniesException() throws SQLException {
        Mockito.when(companyDAO.findPerPage(1, 10)).thenThrow(SQLException.class);
        assertNull(facade.getCompanies(1, 10));
        Mockito.verify(companyDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas normal de la fonction GetCompany.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompany() throws SQLException {
        Mockito.when(companyDAO.findById(1L)).thenReturn(company);
        assertTrue(facade.getCompany(1L).getName().equals("test"));
        Mockito.verify(companyDAO).findById(1L);
    }

    /**
     * Teste la fonction GetCompany avec un id négatif.
     */
    @Test
    public void testGetCompanyWithBadId() {
        Company company = facade.getCompany(-1L);
        assertNull(company);
    }

    /**
     * Teste la fonction GetCompany quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCompanyException() throws SQLException {
        Mockito.when(companyDAO.findById(1L)).thenThrow(SQLException.class);
        Company company = facade.getCompany(1);
        assertNull(company);
        Mockito.verify(companyDAO).findById(1L);
    }

    /**
     * Teste le cas normal de la fonction GetComputerDetails.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputerDetails() throws SQLException {
        Mockito.when(computerDAO.findById(1L)).thenReturn(computer);
        assertTrue(facade.getComputerDetails(1L).getName().equals("test"));
        Mockito.verify(computerDAO).findById(1L);
    }

    /**
     * Teste la fonction GetComputer avec un id négatif.
     */
    @Test
    public void testGetComputerWithBadId() {
        Computer computer = facade.getComputerDetails(-1L);
        assertNull(computer);
    }

    /**
     * Teste la fonction GetComputer quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputerException() throws SQLException {
        Mockito.when(computerDAO.findById(1L)).thenThrow(SQLException.class);
        assertNull(facade.getComputerDetails(1));
        Mockito.verify(computerDAO).findById(1L);
    }

    /**
     * Teste le cas normal de la fonction CreateComputer.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     */
    @Test
    public void testCreateComputer() throws SQLException, NoObjectException {
        Mockito.when(computerDAO.add(computer)).thenReturn(1L);
        assertTrue(facade.createComputer(computer) == 1L);
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste la fonction Create avec un objet null.
     */
    @Test
    public void testCreateComputerNull() {
        assertTrue(0 == facade.createComputer(null));
    }

    /**
     * Teste la fonction Create avec un nom null.
     */
    @Test
    public void testCreateComputerNameNull() {
        computer.setName(null);
        assertTrue(0 == facade.createComputer(computer));
    }

    /**
     * Teste la fonction Create avec une mauvaise company.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testCreateComputerBadCompany() throws SQLException {
        computer.setManufacturer(new Company(60L, null));
        Mockito.when(companyDAO.isExist(60L)).thenReturn(false);
        assertTrue(0 == facade.createComputer(computer));
        Mockito.verify(companyDAO).isExist(60L);
    }

    /**
     * Teste la fonction create avec des mauvaises dates.
     */
    @Test
    public void testCreateComputerBadDates() {
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
        assertTrue(0 == facade.createComputer(computer));
    }

    /**
     * Teste la fonction CreateComputer quand une exception SQL est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testCreateComputerExceptionSQL() throws SQLException, NoObjectException {
        Mockito.when(computerDAO.add(computer)).thenThrow(SQLException.class);
        assertTrue(0 == facade.createComputer(computer));
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste la fonction CreateComputer quand une exception NoObject est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testCreateComputerExceptionNoObject() throws SQLException, NoObjectException {
        Mockito.when(computerDAO.add(computer)).thenThrow(NoObjectException.class);
        assertTrue(0 == facade.createComputer(computer));
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste le cas normal de la fonction UpdateComputer.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     */
    @Test
    public void testUpdateComputer() throws SQLException, NoObjectException {
        computer.setId(1L);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        Mockito.when(computerDAO.update(computer)).thenReturn(computer);
        assertTrue(facade.updateComputer(computer).equals(computer));
        Mockito.verify(computerDAO).isExist(1L);
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction Update avec un objet null.
     */
    @Test
    public void testUpdateComputerNull() {
        assertNull(facade.updateComputer(null));
    }

    /**
     * Teste la fonction Update avec un nom null.
     */
    @Test
    public void testUpdateComputerNameNull() {
        computer.setId(1L);
        computer.setName(null);
        assertNull(facade.updateComputer(computer));
    }

    /**
     * Teste la fonction Update avec une mauvaise company.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testUpdateComputerBadCompany() throws SQLException {
        computer.setId(1L);
        computer.setManufacturer(new Company(60L, null));
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        Mockito.when(companyDAO.isExist(60L)).thenReturn(false);
        assertNull(facade.updateComputer(computer));
        Mockito.verify(computerDAO).isExist(1L);
        Mockito.verify(companyDAO).isExist(60L);
    }

    /**
     * Teste la fonction update avec un mauvais id.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testUpdateComputerBadId() throws SQLException {
        computer.setId(1L);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(false);
        assertNull(facade.updateComputer(computer));
        Mockito.verify(computerDAO).isExist(1L);
    }

    /**
     * Teste la fonction Update avec de mauvaises dates.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testUpdateComputerBadDates() throws SQLException {
        computer.setId(1L);
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        assertNull(facade.updateComputer(computer));
        Mockito.verify(computerDAO).isExist(1L);
    }

    /**
     * Teste la fonction UpdateComputer quand une exception SQL est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateComputerExceptionSQL() throws SQLException, NoObjectException {
        computer.setId(1L);
        Mockito.when(computerDAO.update(computer)).thenThrow(SQLException.class);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        assertNull(facade.updateComputer(computer));
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction UpdateComputer quand une exception NoObject est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateComputerExceptionNoObject() throws SQLException, NoObjectException {
        computer.setId(1L);
        Mockito.when(computerDAO.update(computer)).thenThrow(NoObjectException.class);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        assertNull(facade.updateComputer(computer));
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction GetInstance.
     */
    @Test
    public void testGetInstance() {
        Facade facade = Facade.getInstance();
        assertNotNull(facade);
    }

    /**
     * Teste le cas normal de la fonction DeleteComputer.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteComputer() throws SQLException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(true);
        assertTrue(facade.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand il n'y a pas de correspodance.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteComputerNoComputer() throws SQLException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(false);
        assertFalse(facade.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand l'id est négatif.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteComputerBadIdInf() throws SQLException {
        assertFalse(facade.deleteComputer(-1L));
    }

    /**
     * Teste la fonction DeleteComputer quand la DAO lance une exception.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteComputerException() throws SQLException {
        Mockito.when(computerDAO.delete(1L)).thenThrow(SQLException.class);
        assertFalse(facade.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste le cas normal de la fonction getCountComputers.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testCount() throws SQLException {
        Mockito.when(computerDAO.count()).thenReturn(1);
        assertTrue(facade.getCountComputers() == 1);
        Mockito.verify(computerDAO).count();
    }

    /**
     * Teste la fonction getCountComputers quand elle gère l'exception SQL.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testCountException() throws SQLException {
        Mockito.when(computerDAO.count()).thenThrow(SQLException.class);
        assertTrue(facade.getCountComputers() == 0);
        Mockito.verify(computerDAO).count();
    }

}
