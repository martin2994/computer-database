package com.excilys.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.dao.DAOFactory;
import com.excilys.cdb.exceptions.NoObjectException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.DateMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * DAO des Computer Regroupe l'ensemble des transactions sur les computer.
 */
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
     * Requete pour l'insert.
     */
    private final String INSERT_COMPUTER = "INSERT INTO computer (name,introduced,discontinued,company_id) values (?,?,?,?)";

    /**
     * Requete pour le nombre de page.
     */
    private final String MAX_PAGE = "SELECT COUNT(id) FROM computer";

    /**
     * Requete pour le nombre de page d'une recherche.
     */
    private final String MAX_PAGE_BY_NAME = "SELECT COUNT(computer.id) FROM computer LEFT OUTER JOIN company ON computer.company_id=company.id WHERE computer.name LIKE ? or company.name LIKE ? ";

    /**
     * le singleton.
     */
    private static ComputerDAO computerDAO;

    /**
     * Constructeur privé vide.
     */
    private ComputerDAO() {
    }

    /**
     * Récupère la liste de tous les computers.
     * @return la liste des computers
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Override
    public List<Computer> findAll() throws SQLException {
        List<Computer> computers = new ArrayList<>();
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(ALL_COMPUTERS, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement.executeQuery()) {
            computers = ComputerMapper.convertListComputerSQLToListComputer(rs);
        }
        return computers;
    }

    /**
     * Permet de récupérer la liste de tous les computer page par page.
     * @param page
     *            la page à afficher
     * @param resultPerPage
     *            nombre de company par page
     * @return La liste des computer
     */
    @Override
    public Page<Computer> findPerPage(int page, int resultPerPage) throws SQLException {
        Page<Computer> computers = new Page<>();
        if (page >= 0 && resultPerPage >= 1) {
            try (Connection connection = DAOFactory.getConnection();
                    PreparedStatement statement = connection.prepareStatement(ALL_COMPUTERS_PER_PAGE,
                            ResultSet.CONCUR_READ_ONLY)) {
                computers.setResultPerPage(resultPerPage);
                statement.setInt(1, page * resultPerPage);
                statement.setInt(2, resultPerPage);
                try (ResultSet rs = statement.executeQuery()) {
                    computers = ComputerMapper.convertListComputerSQLToPageComputer(rs);
                }
            }
            computers.setMaxPage(count());
            computers.setCurrentPage(page);
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
     * @throws SQLException
     *             Exception SQL lancée
     */
    public Page<Computer> findByNamePerPage(String search, int page, int resultPerPage) throws SQLException {
        Page<Computer> computers = new Page<>();
        if (page >= 0 && resultPerPage >= 1) {
            try (Connection connection = DAOFactory.getConnection();
                    PreparedStatement statement = connection.prepareStatement(COMPUTERS_BY_NAME,
                            ResultSet.CONCUR_READ_ONLY)) {
                statement.setString(1, "%" + search + "%");
                statement.setString(2, "%" + search + "%");
                computers.setResultPerPage(resultPerPage);
                statement.setInt(3, page * resultPerPage);
                statement.setInt(4, resultPerPage);
                try (ResultSet rs = statement.executeQuery()) {
                    computers = ComputerMapper.convertListComputerSQLToPageComputer(rs);
                }
            }
            computers.setMaxPage(count());
            computers.setCurrentPage(page);
        }
        return computers;
    }

    /**
     * Récupère un computer particulier.
     * @param id
     *            l'id du computer à rechercher
     * @return Le computer correspondant
     */
    @Override
    public Optional<Computer> findById(long id) throws SQLException {
        Optional<Computer> computer = Optional.empty();
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(COMPUTER_BY_ID, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                computer = ComputerMapper.convertComputerSQLToComputer(rs);
            }
            return computer;
        }
    }

    /**
     * Ajoute un computer dans la base.
     * @param computer
     *            les informations du computer à ajouter
     * @throws NoObjectException
     *             Exception lancée quand un objet est null ou inexistant
     * @throws SQLException
     *             Exception SQL lancée
     *
     */
    @Override
    public long add(Computer computer) throws SQLException, NoObjectException {
        if (computer != null) {
            try (Connection connection = DAOFactory.getConnection();
                    PreparedStatement statement = connection.prepareStatement(INSERT_COMPUTER,
                            Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, computer.getName());
                Timestamp date = null;
                if (computer.getIntroduced() != null) {
                    date = DateMapper.convertLocalDateToTimeStamp(computer.getIntroduced());
                }
                statement.setTimestamp(2, date);
                date = null;
                if (computer.getDiscontinued() != null) {
                    date = DateMapper.convertLocalDateToTimeStamp(computer.getDiscontinued());
                }
                statement.setTimestamp(3, date);
                if (computer.getManufacturer() != null) {
                    statement.setLong(4, computer.getManufacturer().getId());
                } else {
                    statement.setObject(4, null);
                }
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
                return 0;
            }
        } else {
            throw new NoObjectException("Pas d'ordinateur à ajouter");
        }
    }

    /**
     * Regarde si le computer existe.
     * @param id
     *            le computer à verifier
     * @return un booleen avec la réponse
     * @throws SQLException
     *             Exception SQL lancée
     */
    @Override
    public boolean isExist(long id) throws SQLException {
        if (id >= 0) {
            try (Connection connection = DAOFactory.getConnection();
                    PreparedStatement statement = connection.prepareStatement(COMPUTER_EXIST,
                            ResultSet.CONCUR_READ_ONLY)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Supprime un computer de la base.
     * @param id
     *            l'id du computer à supprimer
     * @return Si la suppression a été effectuée
     */
    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_COMPUTER)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            if (result == 0) {
                return false;
            }
            return true;
        }
    }

    /**
     * Permet de supprimer une liste d'id sous forme de string.
     * @param idList
     *            la liste d'id
     * @return un boolean pour savoir si la suppression a eu lieu ou non
     * @throws SQLException
     *             Exception SQL lancée
     */
    public boolean deleteList(String idList) throws SQLException {
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(String.format(DELETE_COMPUTER_LIST, idList))) {
            int result = statement.executeUpdate();
            if (result == 0) {
                return false;
            }
            return true;
        }
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
    public Optional<Computer> update(Computer computer) throws SQLException, NoObjectException {
        Optional<Computer> optComputer = Optional.empty();
        if (computer != null) {
            try (Connection connection = DAOFactory.getConnection();
                    PreparedStatement statement = connection.prepareStatement(UPDATE_COMPUTER)) {
                statement.setString(1, computer.getName());
                Timestamp date = null;
                if (computer.getIntroduced() != null) {
                    date = DateMapper.convertLocalDateToTimeStamp(computer.getIntroduced());
                }
                statement.setTimestamp(2, date);
                date = null;
                if (computer.getDiscontinued() != null) {
                    date = DateMapper.convertLocalDateToTimeStamp(computer.getDiscontinued());
                }
                statement.setTimestamp(3, date);
                if (computer.getManufacturer() != null) {
                    statement.setLong(4, computer.getManufacturer().getId());
                } else {
                    statement.setObject(4, null);
                }
                statement.setLong(5, computer.getId());
                int result = statement.executeUpdate();
                if (result == 1) {
                    optComputer = Optional.ofNullable(computer);
                }
            }
        } else {
            throw new NoObjectException("Pas de computer à mettre à jour");
        }
        return optComputer;
    }

    /**
     * Récupère le nombre de d'élement total.
     */
    @Override
    public int count() throws SQLException {
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(MAX_PAGE);
                ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    /**
     * Récupère le nombre de d'élement d'une recherche.
     * @param search
     *            le nom à rechercher
     * @return le nombre de computer
     * @throws SQLException
     *             Exception SQL lancée
     */
    public int countByName(String search) throws SQLException {
        try (Connection connection = DAOFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(MAX_PAGE_BY_NAME)) {
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    /**
     * Récupère le singleton computerDAO.
     * @return le ComputerDAO
     */
    public static ComputerDAO getInstance() {
        if (computerDAO == null) {
            computerDAO = new ComputerDAO();
        }
        return computerDAO;
    }

}
