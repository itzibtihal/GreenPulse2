package repositories;

import db.DbConnection;
import entities.*;
import entities.enums.EnergyType;
import entities.enums.FoodType;
import entities.enums.VehicleType;
import exceptions.DatabaseConnectionException;
import exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserRepository {

    private Connection connection;

    // Constructor that initializes the Connection
    public  UserRepository() {
        this.connection = DbConnection.getConnection();
    }

    //find user
    public Optional<User> findById(UUID id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getTimestamp("date_created").toLocalDateTime()
                );
                return Optional.of(user);  // Return Optional with the found user
            } else {
                return Optional.empty();  // Return an empty Optional if no user is found
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching user from the database.", e);
        }
    }

    //list all users
    public List<User> findAll() {
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


    //save user
    public void save(User user) {
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

    //update

    //find max
    public Optional<Double> findMaxCarbonConsumption(UUID userId) {
        String query = "SELECT MAX(carbon_consumption) FROM consumptions WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.ofNullable(resultSet.getDouble(1)); // Handle nullable cases
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching max carbon consumption.", e);
        }
    }

    //delete
    public Optional<User> delete(UUID id) {
        String query = "DELETE FROM users WHERE id = ? RETURNING *";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User deletedUser = new User(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getTimestamp("date_created").toLocalDateTime()
                );
                return Optional.of(deletedUser);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error deleting user.", e);
        }
    }


    //update
    public Optional<User> update(UUID id, String newName, Integer newAge) {
        StringBuilder query = new StringBuilder("UPDATE users SET ");
        boolean updateName = newName != null && !newName.isEmpty();
        boolean updateAge = newAge != null;

        // Build the query based on the user's input
        if (updateName) {
            query.append("name = ?");
        }
        if (updateAge) {
            if (updateName) {
                query.append(", ");
            }
            query.append("age = ?");
        }
        query.append(" WHERE id = ?");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int index = 1;
            if (updateName) {
                statement.setString(index++, newName);  // Set the new name
            }
            if (updateAge) {
                statement.setInt(index++, newAge);     // Set the new age
            }
            statement.setObject(index, id);            // Set the user ID

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return findById(id); // Return the updated user if update was successful
            } else {
                return Optional.empty(); // Return empty if no user was updated
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error updating user.", e);
        }
    }

}
