package com.excilys.cdb.services;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.SpringTestConfigurationService;
import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.exceptions.InvalidIdException;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.exceptions.computer.InvalidDateException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfigurationService.class)
public class ComputerServiceTest {

    @Mock
    private CompanyDAO companyDAO;

    @Mock
    private ComputerDAO computerDAO;

    @InjectMocks
    private ComputerService computerService;

    @Autowired
    private ComputerService computerServiceBean;

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
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputers() throws InvalidComputerException {
        List<Computer> computers = Collections.nCopies(5, computer);
        Page<Computer> page = new Page<>();
        page.setResults(computers);
        Mockito.when(computerDAO.findPerPage(1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(computerService.getComputers(1, 10).getResults()));
        Mockito.verify(computerDAO).findPerPage(1, 10);
    }

    /**
     * Teste le cas ou le nombre de computers par page est négatif.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputersBadResultPerPage() throws InvalidComputerException {
        assertTrue(computerServiceBean.getComputers(0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetComputers quand la page est négative.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputersWithBadPageInf() throws InvalidComputerException {
        Page<Computer> computers = computerServiceBean.getComputers(-1, 10);
        assertTrue(computers.getResults().isEmpty());
    }

    /**
     * Teste le cas normal de la fonction GetComputersByName.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputersByName() throws InvalidComputerException {
        List<Computer> computers = Collections.nCopies(5, computer);
        Page<Computer> page = new Page<>();
        page.setResults(computers);
        Mockito.when(computerDAO.findByNamePerPage("test", 1, 10)).thenReturn(page);
        assertTrue(page.getResults().equals(computerService.getComputersByName("test", 1, 10).getResults()));
        Mockito.verify(computerDAO).findByNamePerPage("test", 1, 10);
    }

    /**
     * Teste le cas ou le nombre de computers par page est négatif.
     * @throws InvalidComputerException
     *                  Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputersByNameBadResultPerPage() throws InvalidComputerException {
        assertTrue(computerServiceBean.getComputersByName("test", 0, -1).getResults().isEmpty());
    }

    /**
     * Teste la fonction GetComputersByName quand la page est négative.
     * @throws InvalidComputerException
     *                  Exception lancée quand la requete echoue
     */
    @Test
    public void testGetComputersbyNameWithBadPageInf() throws InvalidComputerException {
        Page<Computer> computers = computerServiceBean.getComputersByName("test", -1, 10);
        assertTrue(computers.getResults().isEmpty());
    }

    /**
     * Teste le cas normal de la fonction GetComputerDetails.
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     * @throws NoObjectException
     *              Exception lancée quand la requete échoue
     * @throws InvalidIdException 
     */
    @Test
    public void testGetComputerDetails() throws InvalidComputerException, NoObjectException, InvalidIdException {
        Mockito.when(computerDAO.findById(1L)).thenReturn(Optional.ofNullable(computer));
        assertTrue("test".equals(computerService.getComputerDetails(1L).getName()));
        Mockito.verify(computerDAO).findById(1L);
    }

    /**
     * Teste la fonction GetComputer avec un id négatif.
     * @throws InvalidComputerException
     *             Exception lancée quand le computer n'est pas valide
     * @throws NoObjectException 
     * @throws InvalidIdException 
     */
    @Test
    public void testGetComputerWithBadId() throws InvalidComputerException, NoObjectException, InvalidIdException {
    	exception.expect(InvalidIdException.class);
        computerServiceBean.getComputerDetails(-1L);
    }

    /**
     * Teste le cas normal de la fonction CreateComputer.
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputer() throws NoObjectException, InvalidComputerException, InvalidCompanyException, InvalidIdException {
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
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputerNull() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
    	exception.expect(InvalidComputerException.class);
        computerServiceBean.createComputer(null);
    }

    /**
     * Teste la fonction Create avec un nom null.
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputerNameNull() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
        computer.setName(null);
    	exception.expect(InvalidComputerException.class);
        computerServiceBean.createComputer(computer);
    }

    /**
     * Teste la fonction Create avec une mauvaise company.
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     * @throws NoObjectException
     *              Exception lancée quand la requete echoue (pas de resultat)
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputerBadCompany() throws InvalidComputerException, InvalidCompanyException, NoObjectException, InvalidIdException {
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
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputerBadDates() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
    	exception.expect(InvalidDateException.class);
    	computerServiceBean.createComputer(computer);
    }

    /**
     * Teste la fonction CreateComputer quand une exception NoObject est lancée.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidComputerException
     *             Exception lancée quand les infos du computer sont invalides
     * @throws InvalidCompanyException
     *             Exception lancée quand la company est invalide
     * @throws InvalidIdException 
     */
    @Test
    public void testCreateComputerExceptionNoObject()
            throws NoObjectException, InvalidComputerException, InvalidCompanyException, InvalidIdException {
        Mockito.when(computerDAO.add(computer)).thenThrow(NoObjectException.class);
    	exception.expect(InvalidComputerException.class);
        computerService.createComputer(computer);
        Mockito.verify(computerDAO).add(computer);
    }

    /**
     * Teste le cas normal de la fonction UpdateComputer.
     * @throws NoObjectException
     *             Exception lancée quand un objet est à null ou inexistant
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputer() throws NoObjectException, InvalidComputerException, InvalidCompanyException, InvalidIdException {
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
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerNull() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
    	exception.expect(InvalidComputerException.class);
        computerServiceBean.updateComputer(null);
    }

    /**
     * Teste la fonction Update avec un nom null.
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerNameNull() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
        computer.setId(1L);
        computer.setName(null);
    	exception.expect(InvalidComputerException.class);
        computerServiceBean.updateComputer(computer);
    }

    /**
     * Teste la fonction Update avec une mauvaise company.
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws NoObjectException
     *              Exception lancée quand la requete echoue ( pas de resultat)
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerBadCompany() throws InvalidComputerException, InvalidCompanyException, NoObjectException, InvalidIdException {
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
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws NoObjectException
     *              Exception lancée quand la requete echoue ( pas de resultat)
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerBadId() throws InvalidComputerException, InvalidCompanyException, NoObjectException, InvalidIdException {
        computer.setId(1L);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(false);
    	exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).isExist(1L);
    }

    /**
     * Teste la fonction Update avec de mauvaises dates.
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerBadDates() throws InvalidComputerException, InvalidCompanyException, InvalidIdException {
        computer.setId(1L);
        computer.setIntroduced(LocalDate.of(2020, 1, 1));
        computer.setDiscontinued(LocalDate.of(2010, 1, 1));
    	exception.expect(InvalidDateException.class);
        computerServiceBean.updateComputer(computer);
    }

    /**
     * Teste la fonction UpdateComputer quand une exception NoObject est lancée.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws InvalidCompanyException
     *             Exception lancée quand le computer n'est pas valide
     * @throws InvalidComputerException
     *             Exception lancée quand la company n'est pas valide
     * @throws InvalidIdException 
     */
    @Test
    public void testUpdateComputerExceptionNoObject()
            throws NoObjectException, InvalidComputerException, InvalidCompanyException, InvalidIdException {
        computer.setId(1L);
        Mockito.when(computerDAO.update(computer)).thenThrow(NoObjectException.class);
        Mockito.when(computerDAO.isExist(1L)).thenReturn(true);
    	exception.expect(InvalidComputerException.class);
        computerService.updateComputer(computer);
        Mockito.verify(computerDAO).update(computer);
    }

    /**
     * Teste le cas normal de la fonction DeleteComputer.
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputer() throws InvalidIdException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(true);
        assertTrue(computerService.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand il n'y a pas de correspodance.
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputerNoComputer() throws InvalidIdException {
        Mockito.when(computerDAO.delete(1L)).thenReturn(false);
        assertFalse(computerService.deleteComputer(1L));
        Mockito.verify(computerDAO).delete(1L);

    }

    /**
     * Teste la fonction DeleteComputer quand l'id est négatif.
     * @throws InvalidIdException
     *             Exception sur l'id d'un computer
     */
    @Test
    public void testDeleteComputerBadIdInf() throws InvalidIdException {
    	exception.expect(InvalidIdException.class);
        computerServiceBean.deleteComputer(-1L);
    }

    /**
     * Teste la cas normal de la fonction DeleteList.
     */
    @Test
    public void testDeleteList() {
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        Mockito.when(computerDAO.deleteList(ids)).thenReturn(true);
        assertTrue(computerService.deleteComputerList(ids));
        Mockito.verify(computerDAO).deleteList(ids);
    }

    /**
     * Teste le cas normal de la fonction getCountComputers.
     */
    @Test
    public void testCount() {
        Mockito.when(computerDAO.count()).thenReturn(1);
        assertTrue(computerService.getCountComputers() == 1);
        Mockito.verify(computerDAO).count();
    }

    /**
     * Teste le cas normal de la fonction getCountComputersByName.
     *
     */
    @Test
    public void testGetCountComputersByName() {
        Mockito.when(computerDAO.countByName("Apple")).thenReturn(1);
        assertTrue(computerService.getCountComputersByName("Apple") == 1);
        Mockito.verify(computerDAO).countByName("Apple");
    }
}
