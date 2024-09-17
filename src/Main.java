import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import entities.*;
import repositories.ConsumptionRepository;
import services.ConsumptionService;
import services.UserService;
import views.Menu;

public class Main {

    public static void main(String[] args) {
        Connection connection = DbConnection.getConnection();


        UserService userService = new UserService();
        ConsumptionService consumptionService = new ConsumptionService(connection);


        Menu menu = new Menu(userService, consumptionService);

        menu.displayMainMenu();
    }
}

