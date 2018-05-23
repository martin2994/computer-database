package com.excilys.cdb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Computer;

public class ComputerRowMapper implements RowMapper<Computer> {

    @Override
    public Computer mapRow(ResultSet rs, int line) throws SQLException {
        ComputerResultSetExtractor cExtractor = new ComputerResultSetExtractor();
        return cExtractor.extractData(rs);
    }

}
