package com.excilys.cdb.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.impl.CompanyDAO;
import com.excilys.cdb.dao.impl.ComputerDAO;
import com.excilys.cdb.enums.DAOType;
import com.excilys.cdb.exceptions.NoDAOException;
import com.excilys.cdb.exceptions.NoFactoryException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Fabrique de DAO Singleton retournant les DAO.
 */
public class DAOFactory {

    /**
     * Configuration hikari.
     */
    private static HikariConfig hikariConfig;

    /**
     * Data source hikari.
     */
    private static HikariDataSource hikariDataSource;

    /**
     * Singleton de la fabrique.
     */
    private static DAOFactory factory;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DAOFactory.class);

    /**
     * Constructeur qui crée la connexion à la DB à partir d'un fichier de
     * properties.
     */
    private DAOFactory() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
            Class.forName("com.mysql.jdbc.Driver");
            hikariConfig = new HikariConfig(prop);
            hikariDataSource = new HikariDataSource(hikariConfig);
            hikariDataSource.setLeakDetectionThreshold(60 * 1000);
        } catch (IOException e) {
            LOGGER.warn("PROBLEME DE CONNEXION A LA BD " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.warn("PROBLEME DE DRIVER MYSQL " + e.getMessage());
        }
    }

    /**
     * Permet de récupérer une connexion à partir d'hikari.
     * @return la connexion
     * @throws SQLException
     *             Exception SQL lancée par le récupération de la connexion
     */
    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    /**
     * Permet de récupérer une DAO spécifique.
     * @param type
     *            le type de la DAO voulue
     * @return le DAO voulue
     * @throws NoDAOException
     *             DAO non trouvée
     * @throws NoFactoryException
     *             Fabrique non créée
     */
    public static DAO<?> getDAO(DAOType type) throws NoDAOException, NoFactoryException {
        if (factory == null) {
            factory = new DAOFactory();
        }
        if (type == null) {
            throw new NoDAOException("pas de type donné");
        }
        switch (type) {
        case COMPANY:
            return CompanyDAO.getInstance();
        case COMPUTER:
            return ComputerDAO.getInstance();
        default:
            throw new NoDAOException("impossible de trouver une DAO");
        }
    }

}
