package com.excilys.cdb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.excilys.cdb.mapper.DateMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerResultSetExtractor implements ResultSetExtractor<Computer> {

    @Override
    public Computer extractData(ResultSet rs) throws SQLException, DataAccessException {
        Company company = new Company(rs.getInt("company.id"), rs.getString("company.name"));
        return new Computer.Builder(rs.getString("computer.name")).id(rs.getInt("computer.id"))
                .introduced(DateMapper.timeStampToLocal(rs.getTimestamp("computer.introduced")))
                .discontinued(DateMapper.timeStampToLocal(rs.getTimestamp("computer.discontinued")))
                .manufacturer(company).build();
    }

}
