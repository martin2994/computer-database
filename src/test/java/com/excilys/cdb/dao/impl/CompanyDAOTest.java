package com.excilys.cdb.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.SpringTestConfiguration;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.company.InvalidCompanyException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.utils.Page;

@SpringJUnitConfig(classes = SpringTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class CompanyDAOTest {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private ComputerDAO computerDAO;

    private Company company;

    // @Rule
    // public final ExpectedException exception = ExpectedException.none();

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAOTest.class);

    /**
     * Crée une company.
     */
    @BeforeEach
    public void setUp() {
        company = new Company();
        company.setName("testCompany");
    }

    /**
     * Met à null la company.
     */
    @AfterEach
    public void tearDown() {
        company = null;
    }



    /**
     * Teste le cas normal de la fonction findById.
     * @throws NoObjectException
     *          Excpetion lancée quand la requete échoue
     */
    @Test
    public void testFindById() throws NoObjectException {
        Company company = companyDAO.findById(1L).get();
        assertTrue("Apple Inc.".equals(company.getName()));
        assertTrue(company.getId() == 1L);
    }

    /**
     * Teste la fonction findById avec un mauvais id.
     */
    @Test
    public void testFindByIdBadId() {
        assertEquals(Optional.empty(), companyDAO.findById(-1L));
    }

    /**
     * Teste le cas normal de la fonction FindAll.
     */
    @Test
    public void testFindAll() {
        List<Company> list = companyDAO.findAll();
        assertTrue(list.size() == 3);
    }

    /**
     * Teste le cas normal de la fonction FindPerPage.
     * @throws InvalidCompanyException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testFindPerPage() throws InvalidCompanyException {
        Page<Company> page = companyDAO.findPerPage(0, 10);
        assertTrue(page.getMaxPage() == 1);
        assertTrue(page.getResults().size() == 3);
    }

    /**
     * Teste la fonction FindPerPage quand le nombre de company par page est
     * négatif.
     */
    @Test
    public void testFindPerPageBadResultPerPage() {
        assertThrows(InvalidCompanyException.class, () -> companyDAO.findPerPage(0, -1));
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessus des limites.
     * @throws InvalidCompanyException
     *              Exception lancée quand la requete echoue
     */
    @Test
    public void testFindPerPagePageSup() throws InvalidCompanyException {
        assertTrue(companyDAO.findPerPage(100, 10).getResults().isEmpty());
    }

    /**
     * Teste la fonction FindPerPage avec une page au dessosu des limites.
     */
    @Test
    public void testFindPerPagePageInf() {
        assertThrows(InvalidCompanyException.class, () -> companyDAO.findPerPage(-1, 10));
    }

    /**
     * Teste le cas normal de la fonction Delete.
     * @throws NoObjectException
     *          Exception lancée quand la requete échoue
     */
    @Test
    public void testDelete() throws NoObjectException {
        assertTrue(companyDAO.delete(2L));
        assertEquals(Optional.empty(), computerDAO.findById(2L));
        assertEquals(Optional.empty(), companyDAO.findById(2L));
    }

    /**
     * Teste la fonction Delete quand l'objet est inexistant.
     */
    @Test
    public void testDeleteNoComputer() {
        assertFalse(companyDAO.delete(100L));
    }

    /**
     * Teste la fonction Delete quand l'id est negatif.
     */
    @Test
    public void testDeleteNegativeId() {
        assertFalse(companyDAO.delete(-1L));
    }


    /**
     * Teste la fonction Count.
     */
    @Test
    public void testCount() {
        int maxPage = companyDAO.count();
        assertTrue(maxPage == 3);
    }

    /**
     * Teste la fonction isExist.
     * @throws NoObjectException
     *          Exception lancée quand il n'y a pas de resultat
     */
    @Test
    public void testIsExist() throws NoObjectException {
        boolean test = companyDAO.isExist(1L);
        assertTrue(test);
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     */
    @Test
    public void testIsExistWithBadId() {
        assertFalse(companyDAO.isExist(100L));
    }

    /**
     * Teste la fonction isExist avec un mauvais argument.
     */
    @Test
    public void testIsExistBadArgument() {
        assertFalse(companyDAO.isExist(-1L));
    }

    /**
     * Teste le cas normal de la fonction Add.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAdd() throws NoObjectException {
        long id = companyDAO.add(company);
        assertTrue(id == 3);
    }

    /**
     * Teste la fonction Add avec un argument null.
     * @throws NoObjectException
     *             Exception lancée quand un objet null
     */
    @Test
    public void testAddNull() throws NoObjectException {
        assertThrows(NoObjectException.class, () -> companyDAO.add(null));

    }

    /**
     * Teste la fonction Add avec un argument possédent un id.
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     */
    @Test
    public void testAddWithId() throws NoObjectException {
        company.setId(1L);
        long id = companyDAO.add(company);
        assertTrue(id == 4);
    }

    /**
     * Teste la cas normal de la fonction Update.
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdate() throws NoObjectException {
        company.setId(3L);
        company.setName("testUpdate");
        Company company2 = companyDAO.update(company).get();
        assertTrue(company2.equals(company));
    }

    /**
     * Teste la fonction Update sur une company avec un id inexistant.
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateWithInexistantId() throws NoObjectException {
        company.setId(700L);
        assertThrows(NoSuchElementException.class, () -> companyDAO.update(company).get());
    }

    /**
     * Teste la fonction Update sur une company avec un id zero.
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateWithIdZero() throws NoObjectException {
        company.setId(0L);
        assertEquals(Optional.empty(), companyDAO.update(company));
    }

    /**
     * Teste la fonction Update quand la company est null.
     * @throws NoObjectException
     *             Exception lancé quand un objet est null ou inexistant
     */
    @Test
    public void testUpdateNull()  {
        assertThrows(NoObjectException.class, () -> companyDAO.update(null));
    }

}
