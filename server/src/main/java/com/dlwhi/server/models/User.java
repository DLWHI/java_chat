package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class User implements DatabaseModel {
    private static final String tableName = "users";
    
    private Long id;
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void fromResultSet(ResultSet queryResult) 
            throws SQLException {
        id = queryResult.getLong("id");
        username = queryResult.getString("username");
        password = queryResult.getString("password");
    }

    public SqlParameterSource getParamSource() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (id != null) {
            params.addValue(tableName + ".id", id);
        }
        params.addValue(tableName + ".username", username);
        params.addValue(tableName + ".password", password);
        return params;
    }

    public String getUsername() {
        return username;
    }

    public boolean passwdMatches(String passwd) {
        return passwd.equals(password);
    }
}
