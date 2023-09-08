package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface DatabaseModel {
    public void fromResultSet(ResultSet queryResult) throws SQLException;

    public SqlParameterSource getParamSource();
}
