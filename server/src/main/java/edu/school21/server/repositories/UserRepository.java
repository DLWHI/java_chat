package edu.school21.server.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import edu.school21.server.models.User;

public class UserRepository implements IUserRepository {

    private final String findQuery = "select * from users where id = ?";
    private final String findUNameQuery = "select * from users where username = ?";
    private final String insertQuery = "insert into " + 
        "users(username, password) values(?, ?)";
    private final String updateQuery = "update users set " +
        "username = ?, password = ? where id = ?";
    private final String deleteQuery = "delete from users where id = ?;";
    private final String findAll = "select * from users";

    private DataSource DB;

    public UserRepository(DataSource dB) {
        DB = dB;
    }

    @Override
    public User findById(Long id) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(findQuery);
            query.setLong(1, id);
            ResultSet queryResult =  query.executeQuery();
            if (queryResult.next()) {
                return User.fromResultSet(queryResult);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new LinkedList<>();
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(findAll);
            ResultSet queryResult =  query.executeQuery();
            while (queryResult.next()) {
                result.add(User.fromResultSet(queryResult));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public User save(User entity) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(insertQuery);
            query.setString(1, entity.getUsername());
            query.setString(2, entity.getPassword());
            query.executeUpdate();
            entity = findByUsername(entity.getUsername());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void update(User entity) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(updateQuery);
            query.setString(1, entity.getUsername());
            query.setString(2, entity.getPassword());
            query.setLong(3, entity.getId());
            query.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(deleteQuery);
            query.setLong(1, id);
            query.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User findByUsername(String email) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(findUNameQuery);
            query.setString(1, email);
            ResultSet queryResult =  query.executeQuery();
            if (queryResult.next()) {
                return User.fromResultSet(queryResult);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
