package com.excilys.cdb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Company;

public class CompanyRowMapper implements RowMapper<Company> {

    @Override
    public Company mapRow(ResultSet rs, int line) throws SQLException {
        CompanyResultSetExtractor cExtractor = new CompanyResultSetExtractor();
        return cExtractor.extractData(rs);
    }

}
