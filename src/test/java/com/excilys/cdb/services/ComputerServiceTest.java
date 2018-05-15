package com.excilys.cdb.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
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
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidDateException;
import com.excilys.cdb.exceptions.computer.InvalidIdException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

@RunWith(MockitoJUnitRunner.class)
public class ComputerServiceTest {

    @Mock
    private CompanyDAO companyDAO;

    @Mock
    private ComputerDAO computerDAO;

    @InjectMocks
    private ComputerService computerService;

    private Computer computer;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initialise les données Mockito et objet.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        computer = new Computer.Builder("test").build();
    }

    /**
     * Réinitialise les données.
     */
    @After
    public void tearDown() {
        computer = null;
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
        assertTrue(page.getResults().equals(computerService.getComputers(1, 10).getResults()));
        Mockito.verify(computerDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de computers par page est négatif.
     */
    @Test
    public void testGetComputersBadResultPerPage() {
        assertTrue(computerService.getComputers(0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetComputers quand la page est négative.
     */
    @Test
    public void testGetComputersWithBadPageInf() {
        Page<Computer> computers = computerService.getComputers(-1, 10);
        assertTrue(computers.getResults().isEmpty());
    }

    /**
     * Teste l'exception de GetComputer.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputersException() throws SQLException {
        Mockito.when(computerDAO.findPerPage(1, 10)).thenThrow(SQLException.class);
        assertTrue(computerService.getComputers(1, 10).getResults().isEmpty());
        Mockito.verify(computerDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas normal de la fonction GetComputersByName.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputersByName() throws SQLException {
        List<Computer> computers = Collections.nCopies(5, computer);
        Page<Computer> page = new Page<>();
        page.setResults(computers);
        Mockito.when(computerDAO.findByNamePerPage("test", 1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(computerService.getComputersByName("test", 1, 10).getResults()));
        Mockito.verify(computerDAO).findByNamePerPage("test", 1, 10);
    }

    /**
     * Teste le cas ou le nombre de computers par page est négatif.
     */
    @Test
    public void testGetComputersByNameBadResultPerPage() {
        assertTrue(computerService.getComputersByName("test", 0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetComputersByName quand la page est négative.
     */
    @Test
    public void testGetComputersbyNameWithBadPageInf() {
        Page<Computer> computers = computerService.getComputersByName("test", -1, 10);
        assertTrue(computers.getResults().isEmpty());
    }

    /**
     * Teste l'exception de GetComputersByName.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetComputersByNameException() throws SQLException {
        Mockito.when(computerDAO.findByNamePerPage("test", 1, 10)).thenThrow(SQLException.class);
        assertTrue(computerService.getComputersByName("test", 1, 10).getResults().isEmpty());
        Mockito.verify(computerDAO).findByNamePerPage("test", 1, 10);
    }

    /**
     * Teste le cas normal de la fonction GetComputerDetails.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     */
    @Test
    public void testGetComputerDetails() throws SQLException, InvalidComputerException {
        Mockito.when(computerDAO.findById(1L)).thenReturn(Optional.ofNullable(computer));
        assertTrue("test".equals(computerService.getComputerDetails(1L).getName()));
        Mockito.verify(computerDAO).findById(1L);
    }

    /**
     * Teste la fonction GetComputer avec un id négatif.
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     */
    @Test
    public void testGetComputerWithBadId() throws InvalidComputerException {
        exception.expect(InvalidIdException.class);
        computerService.getComputerDetails(-1L);
    }

    /**
     * Teste la fonction GetComputer quand une exception est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     */
    @Test
    public void testGetComputerException() throws SQLException, InvalidComputerException {
        Mockito.when(computerDAO.findById(1L)).thenThrow(SQLException.class);
        exception.expect(InvalidComputerException.class);
        computerService.getComputerDetails(1);
        Mockito.verify(computerDAO).findById(1L);
    }

    /**
     * Teste le cas normal de la fonction CreateComputer.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputer()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        Mockito.when(computerDAO.add(computer)).thenReturn(1L);
        assertTrue(computerService.createComputer(computer) == 1L);
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste la fonction Create avec un objet null.
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerNull() throws InvalidComputerException, InvalidCompanyException {
        exception.expect(InvalidComputerException.class);
        computerService.createComputer(null);
    }

    /**
     * Teste la fonction Create avec un nom null.
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerNameNull() throws InvalidComputerException, InvalidCompanyException {
        computer.setName(null);
        exception.expect(InvalidComputerException.class);
        computerService.createComputer(computer);
    }

    /**
     * Teste la fonction Create avec une mauvaise company.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerBadCompany() throws SQLException, InvalidComputerException, InvalidCompanyException {
        computer.setManufacturer(new Company(60L, null));
        Mockito.when(companyDAO.isExist(60L)).thenReturn(false);
        exception.expect(InvalidCompanyException.class);
        computerService.createComputer(computer);
        Mockito.verify(companyDAO).isExist(60L);
    }

    /**
     * Teste la fonction create avec des mauvaises dates.
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerBadDates() throws InvalidComputerException, InvalidCompanyException {
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
        exception.expect(InvalidComputerException.class);
        computerService.createComputer(computer);
    }

    /**
     * Teste la fonction CreateComputer quand une exception SQL est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerExceptionSQL()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        Mockito.when(computerDAO.add(computer)).thenThrow(SQLException.class);
        assertTrue(0 == computerService.createComputer(computer));
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste la fonction CreateComputer quand une exception NoObject est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     */
    @Test
    public void testCreateComputerExceptionNoObject()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        Mockito.when(computerDAO.add(computer)).thenThrow(NoObjectException.class);
        assertTrue(0 == computerService.createComputer(computer));
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste le cas normal de la fonction UpdateComputer.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputer()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        Mockito.when(computerDAO.update(computer)).thenReturn(Optional.ofNullable(computer));
        assertTrue(computerService.updateComputer(computer).equals(computer));
        Mockito.verify(computerDAO).isExist(1L);
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction Update avec un objet null.
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerNull() throws InvalidComputerException, InvalidCompanyException {
        exception.expect(InvalidComputerException.class);
        computerService.updateComputer(null);
    }

    /**
     * Teste la fonction Update avec un nom null.
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerNameNull() throws InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        computer.setName(null);
        exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
    }

    /**
     * Teste la fonction Update avec une mauvaise company.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerBadCompany() throws SQLException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        computer.setManufacturer(new Company(60L, null));
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        Mockito.when(companyDAO.isExist(60L)).thenReturn(false);
        exception.expect(InvalidCompanyException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).isExist(1L);
        Mockito.verify(companyDAO).isExist(60L);
    }

    /**
     * Teste la fonction update avec un mauvais id.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerBadId() throws SQLException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(false);
        exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).isExist(1L);
    }

    /**
     * Teste la fonction Update avec de mauvaises dates.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerBadDates() throws SQLException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
        exception.expect(InvalidDateException.class);
        computerService.updateComputer(computer);
    }

    /**
     * Teste la fonction UpdateComputer quand une exception SQL est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerExceptionSQL()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        Mockito.when(computerDAO.update(computer)).thenThrow(SQLException.class);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction UpdateComputer quand une exception NoObject est lancée.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     */
    @Test
    public void testUpdateComputerExceptionNoObject()
            throws SQLException, NoObjectException, InvalidComputerException, InvalidCompanyException {
        computer.setId(1L);
        Mockito.when(computerDAO.update(computer)).thenThrow(NoObjectException.class);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
        exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste la fonction GetInstance.
     */
    @Test
    public void testGetInstance() {
        ComputerService facade = ComputerService.getInstance();
        assertNotNull(facade);
    }

    /**
     * Teste le cas normal de la fonction DeleteComputer.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputer() throws SQLException, InvalidIdException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(true);
        assertTrue(computerService.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand il n'y a pas de correspodance.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputerNoComputer() throws SQLException, InvalidIdException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(false);
        assertFalse(computerService.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand l'id est négatif.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputerBadIdInf() throws SQLException, InvalidIdException {
        exception.expect(InvalidIdException.class);
        computerService.deleteComputer(-1L);
    }

    /**
     * Teste la fonction DeleteComputer quand la DAO lance une exception.
     * @throws SQLException
     *             Exception SQL lancée
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputerException() throws SQLException, InvalidIdException {
        Mockito.when(computerDAO.delete(1L)).thenThrow(SQLException.class);
        assertFalse(computerService.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la cas normal de la fonction DeleteList.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteList() throws SQLException {
        Mockito.when(computerDAO.deleteList("(1,2)")).thenReturn(true);
        assertTrue(computerService.deleteComputerList("(1,2)"));
        Mockito.verify(computerDAO).deleteList("(1,2)");
    }

    /**
     * Teste la fonction DeleteList quand la DAO renvoie une exception SQL.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testDeleteListSQLException() throws SQLException {
        Mockito.when(computerDAO.deleteList("(1,2)")).thenThrow(SQLException.class);
        assertFalse(computerService.deleteComputerList("(1,2)"));
        Mockito.verify(computerDAO).deleteList("(1,2)");
    }

    /**
     * Teste le cas normal de la fonction getCountComputers.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testCount() throws SQLException {
        Mockito.when(computerDAO.count()).thenReturn(1);
        assertTrue(computerService.getCountComputers() == 1);
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
        assertTrue(computerService.getCountComputers() == 0);
        Mockito.verify(computerDAO).count();
    }

    /**
     * Teste le cas normal de la fonction getCountComputersByName.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testGetCountComputersByName() throws SQLException {
        Mockito.when(computerDAO.countByName("Apple")).thenReturn(1);
        assertTrue(computerService.getCountComputersByName("Apple") == 1);
        Mockito.verify(computerDAO).countByName("Apple");
    }

    /**
     * Teste la fonction getCountByNameComputers quand elle gère l'exception SQL.
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Test
    public void testCountByNameException() throws SQLException {
        Mockito.when(computerDAO.countByName("Apple")).thenThrow(SQLException.class);
        assertTrue(computerService.getCountComputersByName("Apple") == 0);
        Mockito.verify(computerDAO).countByName("Apple");
    }
}
