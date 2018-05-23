package com.excilys.cdb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.excilys.cdb.model.Company;

public class CompanyResultSetExtractor implements ResultSetExtractor<Company> {

    @Override
    public Company extractData(ResultSet rs) throws SQLException, DataAccessException {
        return new Company(rs.getLong("company.id"), rs.getString("company.name"));
    }

}
