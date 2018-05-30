package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.SpringTestConfiguration;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

@SpringJUnitConfig(classes = SpringTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class ComputerDAOTest {

    @Autowired
    private ComputerDAO computerDAO;

    private Computer computerTest;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAOTest.class);

    /**
     * Initialise un computer.
     */
    @BeforeEach
    public void setUp() {
        computerTest = new Computer.Builder("test").build();
    }

    /**
     * Met à null tous les objets.
     */
    @AfterEach
    public void tearDown() {
        computerTest = null;
    }

    /**
     * Teste le cas normal de la fonction FindAll.
     */
    @Test
    public void testFindAll()  {
        List<Computer> list = computerDAO.findAll();
        assertTrue(list.size() == 14);
    }

    /**
     * Teste le cas normal de la fonction findById.
     * @throws NoObjectException
     *              Exception lancé quand la requete échoue
     */
    @Test
    public void testFindById() throws NoObjectException {
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
     */
    @Test
    public void testFindByIdBadId()  {
        assertEquals(Optional.empty(), computerDAO.findById(-1L));
    }

    /**
     * Teste le cas normal de la fonction FindPerPage.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete est mal formée
     */
    @Test
    public void testFindPerPage() throws InvalidComputerException  {
        Page<Computer> page = computerDAO.findPerPage(0, 10);
        assertTrue(page.getMaxPage() == 2);
        assertTrue(page.getResults().size() == 10);
    }

    /**
     * Teste la fonction FindPerPage quand le nombre de computer par page est
     * négatif.
     */
    @Test
    public void testFindPerPageBadResultPerPage()  {
        assertThrows(InvalidComputerException.class, () -> computerDAO.findPerPage(0, -1));
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessus des limites.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testFindPerPagePageSup() throws InvalidComputerException {
        assertTrue(computerDAO.findPerPage(100, 10).getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessous des limites.
     */
    @Test
    public void testFindPerPagePageInf()  {
        assertThrows(InvalidComputerException.class, () -> computerDAO.findPerPage(-1, 10));
    }

    /**
     * Teste la fonction FindByNamePerPage.
     * @throws InvalidComputerException
     *              Exception lancée quand la requete est mal formée
     */
    @Test
    public void testFindPerPageByName() throws InvalidComputerException  {
        Page<Computer> page = computerDAO.findByNamePerPage("MacBook", 0, 10);
        assertTrue(page.getResults().size() == 2);
    }

    /**
     * Teste la fonction FindByNamePerPage avec une page au dessous des limites.
     */
    @Test
    public void testFindByNamePerPagePageInf()  {
        assertThrows(InvalidComputerException.class, () -> computerDAO.findByNamePerPage("Apple", -1, 10));
    }

    /**
     * Teste la fonction FindByNamePerPage avec un resultat par page au dessous des
     * limites.
     */
    @Test
    public void testFindByNamePerPageResultInf()  {
        assertThrows(InvalidComputerException.class, () -> computerDAO.findByNamePerPage("Apple", 10, -1));
    }

    /**
     * Teste le cas normal de la fonction Add.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAdd() throws NoObjectException {
        long id = computerDAO.add(computerTest);
        assertTrue(id == 15);
    }

    /**
     * Teste la fonction Add avec un argument null.
     * @throws NoObjectException
     *             Exception lancée quand un objet null
     */
    @Test
    public void testAddNull() throws NoObjectException {
        assertThrows(NoObjectException.class, () -> computerDAO.add(null));

    }

    /**
     * Teste la fonction Add quand on ajoute une company inexistante.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null
     */
    @Test
    public void testAddWithInexistantCompany() throws NoObjectException {
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
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAddWithId() throws NoObjectException {
        computerTest.setId(1L);
        long id = computerDAO.add(computerTest);
        assertTrue(id == 16);
    }

    /**
     * Teste la fonction Add avec des dates.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAddDate() throws NoObjectException {
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
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdate() throws NoObjectException {
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
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateWithInexistantId() throws NoObjectException {
        Computer computer = computerTest;
        computer.setId(700L);
        assertThrows(NoSuchElementException.class, () -> computerDAO.update(computer).get());
    }

    /**
     * Teste la fonction Update sur unr computer avec un id zero.
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateWithIdZero() throws NoObjectException {
        Computer computer = computerTest;
        computer.setId(0L);
        assertEquals(Optional.empty(), computerDAO.update(computer));
    }

    /**
     * Teste la fonction update quand on change avec une company inexistante.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null
     */
    @Test
    public void testUpdateWithInexistantCompany() throws NoObjectException {
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
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateNull()  {
        assertThrows(NoObjectException.class, () -> computerDAO.update(null));
    }

    /**
     * Teste la fonction Update avec des dates.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateDate() throws NoObjectException {
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
     */
    @Test
    public void testDelete()  {
        assertTrue(computerDAO.delete(3L));
        assertEquals(Optional.empty(), computerDAO.findById(3L));
    }

    /**
     * Teste la fonction Delete quand l'objet est inexistant.
     */
    @Test
    public void testDeleteNoComputer()  {
        assertFalse(computerDAO.delete(100L));
    }

    /**
     * Teste le cas normal de la fonction DeleteList.
     */
    @Test
    public void testDeleteList()  {
        assertTrue(computerDAO.deleteList("(8,9)"));
        assertEquals(Optional.empty(), computerDAO.findById(8L));
    }

    /**
     * Teste la fonction DeleteList quand les objets sont inexistants.
     */
    @Test
    public void testDeleteListNoComputer()  {
        assertFalse(computerDAO.deleteList("(100,110)"));
    }

    /**
     * Teste la fonction Count.
     */
    @Test
    public void testCount()  {
        int maxPage = computerDAO.count();
        assertTrue(maxPage == 14);
    }

    /**
     * Teste la fonction CountByName.
     */
    @Test
    public void testCountByName()  {
        int maxPage = computerDAO.countByName("MacBook");
        System.out.println(maxPage);
        assertTrue(maxPage == 2);
    }

    /**
     * Teste la fonction isExist.
     * @throws NoObjectException
     *              Exception lancée quand il n'y a pas de resultat
     */
    @Test
    public void testIsExist() throws NoObjectException  {
        boolean test = computerDAO.isExist(1L);
        assertTrue(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     */
    @Test
    public void testIsExistWithBadId()  {
        assertFalse(computerDAO.isExist(100L));
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     */
    @Test
    public void testIsExistBadArgument() {
        assertFalse(computerDAO.isExist(-1L));
    }

}
