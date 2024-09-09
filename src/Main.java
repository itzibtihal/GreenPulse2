import db.DbConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import entities.*;
import repositories.UserRepository;


public class Main {

    public static void main(String[] args) {
       // Connection connection = DbConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

//        if (connection != null) {
//            try {
//                DatabaseMetaData metaData = connection.getMetaData();
//                System.out.println("Connected to: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
//                System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
//
//                if (connection.isValid(2)) {
//                    System.out.println("The connection is valid .");
//                } else {
//                    System.out.println("The connection is not valid.");
//                }
//            } catch (SQLException e) {
//                System.out.println("Error retrieving database metadata.");
//                e.printStackTrace();
//            } finally {
//                try {
//                    connection.close();
//                    System.out.println("Connection closed.");
//                } catch (SQLException e) {
//                    System.out.println("Failed to close the connection.");
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            System.out.println("Failed to establish a connection.");
//        }
//        UserService userService = new UserService();
//
//        // Test the findUserById method
//        System.out.println("\nTesting findUserById:");
//        try {
//            UUID testId = UUID.fromString("2e217d1c-a189-494e-9833-6140edadb03a"); // Adjust this UUID
//            User user = userService.findUserById(testId);
//            System.out.println("User found: " + user);
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//
//        // Test the findAllUsers method
//        System.out.println("\nTesting findAllUsers:");
//
//            List<User> users = userService.findAllUsers();
//            if (users.isEmpty()) {
//                System.out.println("No users found.");
//            } else {
//                for (User user : users) {
//                    System.out.println(user);
//                }


        UserRepository userRepository = new UserRepository();

        // Example: Find user by ID
//        UUID testId = UUID.fromString("2e217d1c-a189-494f-9833-6140edadb03a");
//        Optional<User> optionalUser = userRepository.findUserById(testId);
//
//        // Handling the optional result
//        optionalUser.ifPresent(user -> System.out.println("User found: " + user));
//        if (optionalUser.isEmpty()) {
//            System.out.println("User with ID " + testId + " not found.");
//        }



        //update
//        System.out.println("Enter the User ID you want to update:");
//        String idInput = scanner.nextLine();
//        UUID userId = UUID.fromString(idInput);
//
//        // Ask if they want to update the name
//        System.out.println("Do you want to update the name? (yes/no):");
//        String updateNameOption = scanner.nextLine();
//        String newName = null;
//        if (updateNameOption.equalsIgnoreCase("yes")) {
//            System.out.println("Enter the new name:");
//            newName = scanner.nextLine();
//        }
//
//        // Ask if they want to update the age
//        System.out.println("Do you want to update the age? (yes/no):");
//        String updateAgeOption = scanner.nextLine();
//        Integer newAge = null;
//        if (updateAgeOption.equalsIgnoreCase("yes")) {
//            System.out.println("Enter the new age:");
//            newAge = scanner.nextInt(); // assuming valid integer input
//        }
//
//        // Call the update method with the provided values
//        Optional<User> updatedUser = userRepository.updateUser(userId, newName, newAge);
//
//        if (updatedUser.isPresent()) {
//            System.out.println("User updated successfully: " + updatedUser.get());
//        } else {
//            System.out.println("User not found or no changes made.");
//        }


        fetchAndDisplayConsumptions(userRepository);





    }


    public static void fetchAndDisplayConsumptions(UserRepository userRepository) {
        UUID userId = UUID.fromString("80ceeb7c-4704-4e3a-bcc3-a6b93fbb08ba"); // Example user ID



        // Retrieve the user from the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Consumption> consumptions = userRepository.findConsumptionsByUserId(userId);

            for (Consumption consumption : consumptions) {
                String type = getConsumptionType(consumption);
                System.out.println("Consumption Type: " + type);
                System.out.println("Details: " + consumption);
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private static String getConsumptionType(Consumption consumption) {
        if (consumption instanceof Food) {
            return "ALIMENTATION";
        } else if (consumption instanceof Housing) {
            return "LOGEMENT";
        } else if (consumption instanceof Transport) {
            return "TRANSPORT";
        }
        throw new IllegalArgumentException("Unknown consumption type");
    }
}
