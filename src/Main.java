import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import entities.*;
import repositories.ConsumptionRepository;

public class Main {
    Connection connection = DbConnection.getConnection();
    public static void main(String[] args) {
        Connection connection = DbConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

//        try {
//            if (connection != null) {
//                System.out.println("Database connection established.");
//
//                // Create an instance of ConsumptionRepository
//                ConsumptionRepository repo = new ConsumptionRepository(connection);
//
//                // Print records before deletion
//                System.out.println("Records before deletion:");
//                try {
//                    printRecords(connection);
//                } catch (SQLException e) {
//                    System.out.println("Error printing records: " + e.getMessage());
//                }
//
//                // Example consumption ID to delete
//                UUID consumptionIdToDelete = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
//
//                // Perform the delete operation
//                try {
//                    repo.delete(consumptionIdToDelete);
//                    System.out.println("Consumption with ID " + consumptionIdToDelete + " deleted successfully.");
//                } catch (SQLException e) {
//                    System.out.println("Error deleting consumption: " + e.getMessage());
//                }
//
//                // Print records after deletion
//                System.out.println("Records after deletion:");
//                try {
//                    printRecords(connection);
//                } catch (SQLException e) {
//                    System.out.println("Error printing records: " + e.getMessage());
//                }
//
//            } else {
//                System.out.println("Failed to establish a connection.");
//            }
//        } finally {
//            // Close the connection
//            if (connection != null) {
//                try {
//                    connection.close();
//                    System.out.println("Connection closed.");
//                } catch (SQLException e) {
//                    System.out.println("Failed to close the connection.");
//                    e.printStackTrace();
//                }
//            }
//        }

        ConsumptionRepository consumptionRepository = new ConsumptionRepository(connection);

        // Fetch users with their consumptions
        System.out.println("Listing all users with their consumptions grouped by type...");
        Map<User, Map<ConsumptionType, List<Consumption>>> usersWithConsumptions = consumptionRepository.findAllUsersWithConsumptions();

        // Print the results
        for (Map.Entry<User, Map<ConsumptionType, List<Consumption>>> entry : usersWithConsumptions.entrySet()) {
            User user = entry.getKey();
            Map<ConsumptionType, List<Consumption>> consumptionsByType = entry.getValue();

            // Print user details
            System.out.println(user.getName() + " (Age: " + user.getAge() + ")");

            // Iterate through each consumption type
            for (Map.Entry<ConsumptionType, List<Consumption>> typeEntry : consumptionsByType.entrySet()) {
                ConsumptionType type = typeEntry.getKey();
                List<Consumption> consumptions = typeEntry.getValue();

                // Print consumption type
                System.out.println("  Consumption Type: " + type);
                if (consumptions.isEmpty()) {
                    System.out.println("    No records for this type.");
                } else {
                    // Print details for each consumption
                    for (Consumption consumption : consumptions) {
                        String typeDetails = getTypeDetails(consumption);
                        System.out.println("    Start Date: " + consumption.getStartDate() +
                                ", End Date: " + consumption.getEndDate() +
                                "\n    type Details: \n" + typeDetails +
                                "\n    Impact: " + consumption.calculateImpact());
                    }
                }
            }
        }


//    private static void printRecords(Connection connection) throws SQLException {
//        String query = "SELECT * FROM consumption";
//        try (PreparedStatement statement = connection.prepareStatement(query);
//             ResultSet resultSet = statement.executeQuery()) {
//
//            while (resultSet.next()) {
//                System.out.printf("ID: %s, User ID: %s, Start Date: %s, End Date: %s, Amount: %f, Type: %s, Impact: %f%n",
//                        resultSet.getObject("id"),
//                        resultSet.getObject("user_id"),
//                        resultSet.getDate("start_date"),
//                        resultSet.getDate("end_date"),
//                        resultSet.getDouble("amount"),
//                        resultSet.getString("type"),
//                        resultSet.getDouble("impact"));
//            }
//        }
//    }
        UUID userId = UUID.fromString("5f4f3e2f-3024-4f75-8dc3-035e66b7f826");


        List<Consumption> consumptions = consumptionRepository.findConsumptionsByUserId(userId);

        // Print the consumption details
        for (Consumption consumption : consumptions) {
            System.out.println(consumption);
        }

    }


    private static String getTypeDetails(Consumption consumption) {
        if (consumption instanceof Transport) {
            Transport t = (Transport) consumption;
            return "    Distance Traveled: " + t.getDistanceTraveled() + " km, Vehicle Type: " + t.getVehicleType();
        } else if (consumption instanceof Housing) {
            Housing h = (Housing) consumption;
            return "    Energy Consumption: " + h.getEnergyConsumption() + " kWh, Energy Type: " + h.getEnergyType();
        } else if (consumption instanceof Food) {
            Food f = (Food) consumption;
            return "    Weight: " + f.getWeight() + " kg, Food Type: " + f.getFoodType();
        } else {
            return "Unknown details";
        }
    }
}
