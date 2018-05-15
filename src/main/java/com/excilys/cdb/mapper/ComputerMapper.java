package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.utils.Page;

/**
 * Possède les différentes fonction de conversion d'un computer SQL vers un
 * computer du modèle.
 * @author martin
 *
 */
public class ComputerMapper {

    /**
     * Permet de convertir la liste des computers de la base SQL en computer du
     * modèle.
     * @param rs
     *            Le ResultSet de la requete
     * @return La liste des computers
     * @throws SQLException
     *             Exception SQL lancée
     */
    public static Page<Computer> convertListComputerSQLToPageComputer(ResultSet rs) throws SQLException {
        Page<Computer> computers = new Page<>();
        while (rs.next()) {
            Company company;
            if (rs.getInt("company.id") > 0) {
                company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
            } else {
                company = null;
            }
            Computer computer = new Computer.Builder(rs.getString("computer.name")).id(rs.getInt("computer.id"))
                    .introduced(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.introduced")))
                    .discontinued(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.discontinued")))
                    .manufacturer(company).build();

            computers.add(computer);
        }
        return computers;
    }

    /**
     * Permet de convertir un computer de la base SQL en computer du modèle.
     * @param rs
     *            Le ResultSet de la requete
     * @return Le computer
     * @throws SQLException
     *             Exception SQL lancée
     */
    public static Optional<Computer> convertComputerSQLToComputer(ResultSet rs) throws SQLException {
        Optional<Computer> computer = Optional.empty();
        if (rs.next()) {
            Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
            computer = Optional
                    .ofNullable(new Computer.Builder(rs.getString("computer.name")).id(rs.getInt("computer.id"))
                            .introduced(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.introduced")))
                            .discontinued(DateMapper.convertTimeStampToLocal(rs.getTimestamp("computer.discontinued")))
                            .manufacturer(company).build());

        }
        return computer;
    }

    /**
     * Permet de transformer une liste d'id et l'avoir dans le bon format.
     * @param listId
     *            la liste des id en string
     * @return la liste sous le bon format
     */
    public static String convertListId(String listId) {
        return Arrays.stream(listId.split(",")).map(stringId -> Long.parseLong(stringId)).collect(Collectors.toSet())
                .toString().replace("[", "(").replace("]", ")");
    }

}
