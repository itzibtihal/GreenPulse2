import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

import entities.*;
import repositories.ConsumptionRepository;

public class Main {

    public static void main(String[] args) {
        Connection connection = DbConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

        try {
            if (connection != null) {
                System.out.println("Database connection established.");

                // Create an instance of ConsumptionRepository
                ConsumptionRepository repo = new ConsumptionRepository(connection);

                // Print records before deletion
                System.out.println("Records before deletion:");
                try {
                    printRecords(connection);
                } catch (SQLException e) {
                    System.out.println("Error printing records: " + e.getMessage());
                }

                // Example consumption ID to delete
                UUID consumptionIdToDelete = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

                // Perform the delete operation
                try {
                    repo.delete(consumptionIdToDelete);
                    System.out.println("Consumption with ID " + consumptionIdToDelete + " deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Error deleting consumption: " + e.getMessage());
                }

                // Print records after deletion
                System.out.println("Records after deletion:");
                try {
                    printRecords(connection);
                } catch (SQLException e) {
                    System.out.println("Error printing records: " + e.getMessage());
                }

            } else {
                System.out.println("Failed to establish a connection.");
            }
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.out.println("Failed to close the connection.");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printRecords(Connection connection) throws SQLException {
        String query = "SELECT * FROM consumption";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                System.out.printf("ID: %s, User ID: %s, Start Date: %s, End Date: %s, Amount: %f, Type: %s, Impact: %f%n",
                        resultSet.getObject("id"),
                        resultSet.getObject("user_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("type"),
                        resultSet.getDouble("impact"));
            }
        }
    }
}
