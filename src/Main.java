import db.DbConnection;

import java.sql.Connection;
import db.DbConnection;
import entities.User;
import services.UserService;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        Connection connection = DbConnection.getConnection();

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
        UserService userService = new UserService();

        // Test the findUserById method
        System.out.println("\nTesting findUserById:");
        try {
            UUID testId = UUID.fromString("2e217d1c-a189-494e-9833-6140edadb03a"); // Adjust this UUID
            User user = userService.findUserById(testId);
            System.out.println("User found: " + user);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Test the findAllUsers method
        System.out.println("\nTesting findAllUsers:");

            List<User> users = userService.findAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                for (User user : users) {
                    System.out.println(user);
                }
            }




    }
}
