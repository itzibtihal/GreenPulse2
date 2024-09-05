import db.DbConnection;

import java.sql.Connection;
import db.DbConnection;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Connection connection = DbConnection.getConnection();

        if (connection != null) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("Connected to: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());

                if (connection.isValid(2)) {
                    System.out.println("The connection is valid .");
                } else {
                    System.out.println("The connection is not valid.");
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving database metadata.");
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.out.println("Failed to close the connection.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Failed to establish a connection.");
        }
    }
}
