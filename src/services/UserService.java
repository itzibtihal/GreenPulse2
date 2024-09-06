package services;

import db.DbConnection;
import entities.User;
import exceptions.DatabaseConnectionException;
import exceptions.UserNotFoundException;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService implements UserRepository {

    private final Connection connection;

    // Constructor that initializes the Connection
    public UserService() {
        this.connection = DbConnection.getConnection();
    }

    @Override
    public User findUserById(UUID id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getTimestamp("date_created").toLocalDateTime()
                );
            } else {
                throw new UserNotFoundException("User with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching user from the database.", e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getTimestamp("date_created").toLocalDateTime()
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching users from the database.", e);
        }

        return users;
    }

    @Override
    public void saveUser(User user) {
        String query = "INSERT INTO users (id, name, age, date_created) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, user.getId());
            statement.setString(2, user.getName());
            statement.setInt(3, user.getAge());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(user.getDateOfCreation()));

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User saved successfully.");
            } else {
                System.out.println("User was not saved.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UUID id) {
        // Implementation here
    }

    @Override
    public List<User> findUsersAboveCarbonLimit(double limit) {
        return List.of();
    }
}
