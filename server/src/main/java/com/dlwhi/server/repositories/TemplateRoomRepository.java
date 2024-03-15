package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.Room;

public class TemplateRoomRepository implements RoomRepository {
    private final String COLUMN_SELECT = 
        "id as rooms.id, " + 
        "name as rooms.name, " + 
        "owner as rooms.ownerId";

    private final String FIND_QUERY_ID =
        "select " + COLUMN_SELECT + " from rooms where id = :id;";
    private final String FIND_QUERY_USERNAME =
        "select " + COLUMN_SELECT + " from rooms where name = :name;";
    private final String INSERT_QUERY =
        "insert into rooms(name, owner) " +
        "values(:name, :ownerId);";
    private final String UPDATE_QUERY = 
        "update rooms set name = :name, owner = :ownerId where id = :id;";
    private final String DELETE_QUERY =
        "delete from rooms where id = :id;";
    private final String GET_ALL_QUERY =
        "select " + COLUMN_SELECT + " from rooms;";

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
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            GET_ALL_QUERY,
            new BeanPropertyRowMapper<Room>(Room.class)
        );
    }

    @Override
    public boolean save(Room entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        try {
            return query.update(
                INSERT_QUERY,
                new MapSqlParameterSource("name", entity.getName())
                    .addValue("ownerId", entity.getOwnerId())
            ) == 1;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public boolean update(Room entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.update(
            UPDATE_QUERY, 
            new MapSqlParameterSource("id", entity.getId())
                .addValue("name", entity.getName())
                .addValue("ownerId", entity.getOwnerId())
        ) == 1;
    }

    @Override
    public void delete(long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(DELETE_QUERY, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<Room> findByName(String name) throws DataAccessException {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            FIND_QUERY_USERNAME,
            new MapSqlParameterSource("name", name),
            new BeanPropertyRowMapper<Room>(Room.class)
        );
    }
}
