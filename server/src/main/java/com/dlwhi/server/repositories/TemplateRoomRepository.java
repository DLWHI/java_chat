package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.Room;

public class TemplateRoomRepository implements RoomRepository {
    private final String COLUMN_SELECT = 
        "id as users.id, " + 
        "username as users.username, " + 
        "password as users.password";

    private final String FIND_QUERY_ID =
        "select " + COLUMN_SELECT + " from users where id = :id;";
    private final String FIND_QUERY_USERNAME =
        "select " + COLUMN_SELECT + " from users where username = :user;";
    private final String INSERT_QUERY =
        "insert into users(username, password) " +
        "values(:user, :passwd);";
    private final String UPDATE_QUERY = 
        "update users set username = :user, password = :passwd where id = :id;";
    private final String DELETE_QUERY =
        "delete from users where id = :id;";
    private final String GET_ALL_QUERY =
        "select " + COLUMN_SELECT + " from users;";

    private final DataSource db;

    public TemplateRoomRepository(DataSource db) {
        this.db = db;
    }

    @Override
    public Room findById(long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        List<Room> found = query.query(
            FIND_QUERY_ID,
            new MapSqlParameterSource("id", id),
            new BeanPropertyRowMapper<Room>(Room.class)
        );
        return (found.isEmpty()) ? null : found.get(0);
    }

    @Override
    public List<Room> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public boolean save(Room entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public boolean update(Room entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Room> findByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }
    
}
