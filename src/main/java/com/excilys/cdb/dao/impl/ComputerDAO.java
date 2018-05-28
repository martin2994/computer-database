package com.excilys.cdb.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.dao.mapper.ComputerRowMapper;
import com.excilys.cdb.enums.ExceptionMessage;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.exceptions.computer.InvalidComputerException;
import com.excilys.cdb.mapper.DateMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * DAO des Computer Regroupe l'ensemble des transactions sur les computers.
 */
@Repository
public class ComputerDAO implements DAO<Computer> {

    /**
     * Requete pour le findAll.
     */
    private final String ALL_COMPUTERS = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";

    /**
     * Requete pour le findPerPage.
     */
    private final String ALL_COMPUTERS_PER_PAGE = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id LIMIT ?,?";

    /**
     * Requete pour le findByNamePerPage.
     */
    private final String COMPUTERS_BY_NAME = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY computer.name LIMIT ?,?";

    /**
     * Requete pour le findById.
     */
    private final String COMPUTER_BY_ID = "SELECT computer.id,computer.name, computer.introduced,computer.discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id=company.id  WHERE computer.id=?";

    /**
     * Requete pour voir l'existance d'un computer.
     */
    private final String COMPUTER_EXIST = "SELECT computer.id FROM computer WHERE computer.id = ?";

    /**
     * Requete pour l'update.
     */
    private final String UPDATE_COMPUTER = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";

    /**
     * Requete pour le delete.
     */
    private final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";

    /**
     * Requete pour le delete d'une liste d'id.
     */
    private final String DELETE_COMPUTER_LIST = "DELETE FROM computer WHERE id IN %s";

    /**
     * Requete pour le nombre de page.
     */
    private final String MAX_PAGE = "SELECT COUNT(id) FROM computer";

    /**
     * Requete pour le nombre de page d'une recherche.
     */
    private final String MAX_PAGE_BY_NAME = "SELECT COUNT(computer.id) FROM computer LEFT OUTER JOIN company ON computer.company_id=company.id WHERE computer.name LIKE ? or company.name LIKE ? ";

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    /**
     * Constructeur privé qui injecte la dataSource.
     * @param dataSource
     *            la dataSource
     */
    @Autowired
    private ComputerDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void initJdbc() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Récupère la liste de tous les computers.
     * @return la liste des computers
     */
    @Override
    public List<Computer> findAll() {
        return jdbcTemplate.query(ALL_COMPUTERS, new ComputerRowMapper());
    }

    /**
     * Permet de récupérer la liste de tous les computers page par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            nombre de company par page
     * @return La liste des computer
     */
    @Override
    public Page<Computer> findPerPage(int page, int resultPerPage) throws InvalidComputerException {
        Page<Computer> computers = new Page<>();
        try {
            computers.setResults(jdbcTemplate.query(ALL_COMPUTERS_PER_PAGE,
                    new Object[] {page * resultPerPage, resultPerPage }, new ComputerRowMapper()));
            computers.setCurrentPage(page);
            computers.setMaxPage(count());
        } catch (BadSqlGrammarException e) {
            String message = ExceptionMessage.BAD_ACCESS.getMessage();
            throw new InvalidComputerException(message);
        }
        return computers;
    }

    /**
     * Permet de récupérer la liste des computers par page avec un nom spécifique.
     * @param search
     *            le nom à rechercher
     * @param page
     *            la page à récupérer
     * @param resultPerPage
     *            le nombre d'élément par page
     * @return la liste des computers
     * @throws InvalidComputerException
     *             Exception lancée quand la requete est mal formée
     */
    public Page<Computer> findByNamePerPage(String search, int page, int resultPerPage)
            throws InvalidComputerException {
        Page<Computer> computers = new Page<>();
        String allSearch = "%" + search + "%";
        try {
            computers.setResults(jdbcTemplate.query(COMPUTERS_BY_NAME,
                    new Object[] {allSearch, allSearch, page * resultPerPage, resultPerPage },
                    new ComputerRowMapper()));
            computers.setMaxPage(countByName(search));
            computers.setCurrentPage(page);
        } catch (BadSqlGrammarException e) {
            String message = ExceptionMessage.BAD_ACCESS.getMessage();
            throw new InvalidComputerException(message);
        }
        return computers;
    }

    /**
     * Récupère un computer particulier.
     * @param id
     *            l'id du computer à rechercher
     * @return Le computer correspondant
     * @throws NoObjectException
     *             Excpetion lancée si la requete échoue
     */
    @Override
    public Optional<Computer> findById(long id) throws NoObjectException {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(COMPUTER_BY_ID, new Object[] {id }, new ComputerRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            String message = ExceptionMessage.NO_RESULT.getMessage();
            throw new NoObjectException(message);
        }
    }

    /**
     * Ajoute un computer dans la base.
     * @param computer
     *            les informations du computer à ajouter
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     *
     */
    @Override
    public long add(Computer computer) throws NoObjectException {
        if (computer == null) {
            String message = ExceptionMessage.NO_COMPUTER_TO_CREATE.getMessage();
            throw new NoObjectException(message);
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource);
        jdbcInsert.withTableName("computer").usingGeneratedKeyColumns("id");
        SqlParameterSource source = new MapSqlParameterSource().addValue("name", computer.getName())
                .addValue("introduced", DateMapper.localDateToTimeStamp(computer.getIntroduced()))
                .addValue("discontinued", DateMapper.localDateToTimeStamp(computer.getDiscontinued()))
                .addValue("company_id", computer.getManufacturer() != null ? computer.getManufacturer().getId() : null);
        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    /**
     * Regarde si le computer existe.
     * @param id
     *            le computer à verifier
     * @return un booleen avec la réponse
     * @throws NoObjectException
     *             Exception lancée quand la requete echoue (pas de resultat)
     */
    @Override
    public boolean isExist(long id) throws NoObjectException {
        boolean result = false;
        try {
            result = jdbcTemplate.queryForObject(COMPUTER_EXIST, new Object[] {id }, Integer.class) > 0;
        } catch (EmptyResultDataAccessException e) {
            String message = ExceptionMessage.NO_RESULT.getMessage();
            throw new NoObjectException(message);
        }
        return result;
    }

    /**
     * Supprime un computer de la base.
     * @param id
     *            l'id du computer à supprimer
     * @return Si la suppression a été effectuée
     */
    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_COMPUTER, new Object[] {id }) == 1;
    }

    /**
     * Permet de supprimer une liste d'id sous forme de string.
     * @param idList
     *            la liste d'id
     * @return un boolean pour savoir si la suppression a eu lieu ou non
     */
    public boolean deleteList(String idList) {
        return jdbcTemplate.update(String.format(DELETE_COMPUTER_LIST, idList)) > 0;
    }

    /**
     * Modifie un computer avec les informations données.
     * @param computer
     *            les informations du computer à modifier
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Override
    public Optional<Computer> update(Computer computer) throws NoObjectException {
        Optional<Computer> computerOpt = Optional.empty();
        if (computer == null) {
            String message = ExceptionMessage.NO_COMPUTER_TO_UPDATE.getMessage();
            throw new NoObjectException(message);
        }
        int result = jdbcTemplate.update(UPDATE_COMPUTER,
                new Object[] {computer.getName(), DateMapper.localDateToTimeStamp(computer.getIntroduced()),
                        DateMapper.localDateToTimeStamp(computer.getDiscontinued()),
                        computer.getManufacturer() != null ? computer.getManufacturer().getId() : null,
                                computer.getId() });
        if (result > 0) {
            computerOpt = Optional.ofNullable(computer);
        }
        return computerOpt;
    }

    /**
     * Récupère le nombre de d'élement total.
     */
    @Override
    public int count() {
        return jdbcTemplate.queryForObject(MAX_PAGE, Integer.class);
    }

    /**
     * Récupère le nombre de d'élement d'une recherche.
     * @param search
     *            le nom à rechercher
     * @return le nombre de computer
     */
    public int countByName(String search) {
        String allSearch = "%" + search + "%";
        return jdbcTemplate.queryForObject(MAX_PAGE_BY_NAME, new Object[] {allSearch, allSearch }, Integer.class);

    }
}
