package com.dlwhi.server.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RoomRowMapper implements RowMapper<Room> {
    private final String ID_COLUMN_LABEL;
    private final String NAME_COLUMN_LABEL;
    private final String OWNERID_COLUMN_LABEL;


    public RoomRowMapper(String idCol, String nameCol, String ownerCol) {
        ID_COLUMN_LABEL = idCol;
        NAME_COLUMN_LABEL = nameCol;
        OWNERID_COLUMN_LABEL = ownerCol;
    }


    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(ID_COLUMN_LABEL);
        String name = rs.getString(NAME_COLUMN_LABEL);
        long owner = rs.getLong(OWNERID_COLUMN_LABEL);
        return new Room(id, name, owner);
    }
}
