package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    private final String ID_COLUMN_LABEL;
    private final String USERNAME_COLUMN_LABEL;
    private final String PASSWD_COLUMN_LABEL;


    public UserRowMapper(String idCol, String usernameCol, String passwdCol) {
        ID_COLUMN_LABEL = idCol;
        USERNAME_COLUMN_LABEL = usernameCol;
        PASSWD_COLUMN_LABEL = passwdCol;
    }


    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(ID_COLUMN_LABEL);
        String name = rs.getString(USERNAME_COLUMN_LABEL);
        String passwd = rs.getString(PASSWD_COLUMN_LABEL);
        return new User(id, name, passwd);
    }
}
