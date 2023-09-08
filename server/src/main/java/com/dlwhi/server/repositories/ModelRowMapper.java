package com.dlwhi.server.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dlwhi.server.models.DatabaseModel;

public class ModelRowMapper<T extends DatabaseModel> implements RowMapper<T> {
    private Class<T> targetClass;

    public ModelRowMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T result = null;
        try {
            result = targetClass.newInstance();
            result.fromResultSet(rs);
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }
    
}
