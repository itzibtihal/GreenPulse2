package views;

import entities.Consumption;
import entities.*;
import entities.Housing;
import entities.User;
import entities.enums.EnergyType;
import entities.enums.FoodType;
import entities.enums.VehicleType;
import exceptions.ConsumptionNotFoundException;
import services.ConsumptionService;
import services.ReportService;
import services.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Menu {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final Scanner scanner;
    private final UserService userService;
    private final ConsumptionService consumptionService;
    private final ReportService reportService;



    public Menu(UserService userService, ConsumptionService consumptionService) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.consumptionService = consumptionService;
        this.reportService = new ReportService(userService, consumptionService);
    }

    public void displayMainMenu() {
        int choice;
        do {
            System.out.println("\n---     Carbon Footprint Tracker Menu     ---");
            System.out.println(" ------------------------------------------------ ");
            System.out.println("    1. Create New User");
            System.out.println("    2. Add Carbon Consumption to User");
            System.out.println("    3. Display User Details");
            System.out.println("    4. List all users");
            System.out.println(" ------------------------------------------------ ");
            System.out.println("    5. User Data Filtering");
            System.out.println("    6. Calculation of Average Consumption");
            System.out.println("    7. Detection of Inactive Users");
            System.out.println("    8. Sorting Users by Consumption");
            System.out.println("    9. Consultation of reports");
            System.out.println(" ------------------------------------------------ ");
            System.out.println("    10. Modify User");
            System.out.println("    11. Delete User");
            System.out.println(" ------------------------------------------------ ");
            System.out.println("    12. Exit");
            System.out.print("\n    Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        addConsumption();
                        break;
                    case 3:
                        displayUserDetails();
                        break;
                    case 4:
                        listAllUsers();
                        break;
                    case 5:
                        handleUserDataFiltering();
                        break;
                    case 6:
                        handleAverageConsumptionCalculation();
                        break;
                    case 7:
                        handleInactiveUsersDetection();
                        break;
                    case 8:
                        displaySortedUsersByCarbonConsumption();
                        break;
                    case 9:
                        generateReport();
                        break;
                    case 10:
                        modifyUser();
                         break;
                    case 11:
                        deleteUser();
                        break;
                    case 12:
                        String message = "Exiting the application...";
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 8);
    }

    private void createUser() {
        System.out.print("    Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("    Enter user age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        User newUser = new User(name, age);
        userService.save(newUser);
        System.out.println(ANSI_GREEN + "   User was successfully created!" + ANSI_RESET);
    }

    public void listAllUsers() {
        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("---     List of Users      ---");
        int idWidth = 36;
        int nameWidth = 20;
        int ageWidth = 3;
        int createdAtWidth = 12;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Formatter for date

        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + createdAtWidth + 9]).replace('\0', '-'));
        System.out.printf("| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "s | %-" + createdAtWidth + "s | %n",
                "User ID", "Name", "Age", "Created At");
        System.out.println(new String(new char[idWidth + nameWidth + ageWidth + createdAtWidth + 9]).replace('\0', '-'));

        for (User user : users) {
            System.out.printf("| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + ageWidth + "d | %-" + createdAtWidth + "s | %n",
                    user.getId(),
                    user.getName(),
                    user.getAge(),
                    user.getDateOfCreation().format(dateFormatter)
            );
            System.out.println(new String(new char[idWidth + nameWidth + ageWidth + createdAtWidth + 9]).replace('\0', '-'));
        }
    }

    private void addConsumption() {
        listAllUsers();
        UUID userId = null;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter user ID to add consumption (UUID format): ");
            try {
                userId = UUID.fromString(scanner.nextLine());
                validID = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }


        System.out.println("Select the type of consumption:");
        System.out.println("1. Transport");
        System.out.println("2. Housing");
        System.out.println("3. Food");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        Consumption consumption = null;

        try {
            switch (typeChoice) {
                case 1:
                    consumption = createTransportConsumption(userId);
                    break;
                case 2:
                    consumption = createHousingConsumption(userId);
                    break;
                case 3:
                    consumption = createFoodConsumption(userId);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            if (consumption != null) {
                consumptionService.save(consumption);
                System.out.println("Consumption added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error saving consumption: " + e.getMessage());
        }


    }
    private Transport createTransportConsumption(UUID userId) {
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the distance traveled (in km): ");
        double distance = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter the vehicle type (CAR/TRAIN): ");
        VehicleType vehicleType = VehicleType.valueOf(scanner.nextLine().toUpperCase());

        UUID consumptionId = UUID.randomUUID();
        double carbonAmount = distance * vehicleType.getCarbonImpact();
        return new Transport(consumptionId, userId, startDate, endDate, carbonAmount, distance, vehicleType);
    }
    private Housing createHousingConsumption(UUID userId) {
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the energy consumption (in kWh): ");
        double energyConsumption = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter the energy type (ELECTRICITY/GAS): ");
        String energyTypeInput = scanner.nextLine();
        EnergyType energyType = EnergyType.valueOf(energyTypeInput.toUpperCase());
        UUID consumptionId = UUID.randomUUID();


        double amount = energyConsumption * energyType.getCarbonImpact();

        return new Housing(consumptionId, userId, startDate, endDate, amount, energyConsumption, energyType);
    }
    private Food createFoodConsumption(UUID userId) {
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the weight of food consumed (in kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter the food type (VEGETABLE/MEAT): ");
        String foodTypeInput = scanner.nextLine();
        FoodType foodType = FoodType.valueOf(foodTypeInput.toUpperCase());
        UUID consumptionId = UUID.randomUUID();
        double amount = weight * foodType.getCarbonImpact();

        return new Food(consumptionId, userId, startDate, endDate, amount, weight, foodType);
    }

    private void displayUserDetails() {
        listAllUsers();
        UUID userID = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter user ID to display details (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }

        System.out.println("Fetching details for the selected user...");

        Map<User, Map<ConsumptionType, List<Consumption>>> userDetails;
        try {
            userDetails = consumptionService.getUserDetailsWithConsumptions(userID);
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching user details.");
            e.printStackTrace();
            return;
        }


        UUID finalUserID = userID;
        User selectedUser = userDetails.keySet().stream()
                .filter(user -> user.getId().equals(finalUserID))
                .findFirst()
                .orElse(null);

        if (selectedUser == null) {
            System.out.println("No user found with the provided ID.");
            return;
        }

        System.out.println("\nUser Details:");
        System.out.println("Name: " + selectedUser.getName());
        System.out.println("Age: " + selectedUser.getAge());

        System.out.println("\nConsumption Details by Type:");
        Map<ConsumptionType, List<Consumption>> consumptionsByType = userDetails.get(selectedUser);

        if (consumptionsByType == null || consumptionsByType.isEmpty()) {
            System.out.println("No consumptions found for this user.");
        } else {
            for (Map.Entry<ConsumptionType, List<Consumption>> typeEntry : consumptionsByType.entrySet()) {
                ConsumptionType type = typeEntry.getKey();
                List<Consumption> consumptions = typeEntry.getValue();

                System.out.println("  Consumption Type: " + type);

                if (consumptions.isEmpty()) {
                    System.out.println("    No records for this type.");
                } else {
                    for (Consumption consumption : consumptions) {
                        String typeDetails = getTypeDetails(consumption);
                        System.out.println("    Start Date: " + consumption.getStartDate() +
                                ", End Date: " + consumption.getEndDate() +
                                "\n    Type Details: \n" + typeDetails +
                                "\n    Impact: " + consumption.calculateImpact());
                    }
                }
            }
        }
    }
    private String getTypeDetails(Consumption consumption) {
        if (consumption instanceof Transport) {
            Transport transport = (Transport) consumption;
            return "Distance Traveled: " + transport.getDistanceTraveled() +
                    ", Vehicle Type: " + transport.getVehicleType();
        } else if (consumption instanceof Housing) {
            Housing housing = (Housing) consumption;
            return "Energy Consumption: " + housing.getEnergyConsumption() +
                    ", Energy Type: " + housing.getEnergyType();
        } else if (consumption instanceof Food) {
            Food food = (Food) consumption;
            return "Weight: " + food.getWeight() +
                    ", Food Type: " + food.getFoodType();
        }
        return "Unknown consumption type";
    }

    private void handleUserDataFiltering() {
        System.out.print("Enter the carbon threshold value: ");
        double threshold = scanner.nextDouble();
        scanner.nextLine();
        try {
            List<User> users = consumptionService.findUsersExceedingCarbonThreshold(threshold);
            if (users.isEmpty()) {
                System.out.println("No users exceed the carbon threshold of " + threshold);
            } else {
                System.out.println("Users exceeding the carbon threshold of " + threshold + ":");
                for (User user : users) {
                    System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving user data.");
            e.printStackTrace();
        }
    }

    private void handleAverageConsumptionCalculation() {
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter the end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        try {
            Map<User, Double> averageConsumptionMap = consumptionService.calculateAverageCarbonConsumptionPerUser(startDate, endDate);

            if (averageConsumptionMap.isEmpty()) {
                System.out.println("No data available for the given period.");
            } else {
                System.out.println("Average Carbon Consumption per User from " + startDate + " to " + endDate + ":");
                for (Map.Entry<User, Double> entry : averageConsumptionMap.entrySet()) {
                    User user = entry.getKey();
                    double averageConsumption = entry.getValue();
                    System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Average Consumption: " + averageConsumption);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while calculating average consumption.");
            e.printStackTrace();
        }
    }

    private void handleInactiveUsersDetection() {
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter the end date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        try {
            List<User> inactiveUsers = consumptionService.findInactiveUsers(startDate, endDate);
            if (inactiveUsers.isEmpty()) {
                System.out.println("No inactive users found for the given period.");
            } else {
                System.out.println("Inactive users from " + startDate + " to " + endDate + ":");
                for (User user : inactiveUsers) {
                    System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving inactive users.");
            e.printStackTrace();
        }
    }


    private void displaySortedUsersByCarbonConsumption() {
        try {

            // Retrieve sorted users
            List<User> sortedUsers = consumptionService.sortUsersByCarbonConsumption();

            // Display the sorted users
            System.out.println("Users sorted by carbon consumption (highest to lowest):");
            for (User user : sortedUsers) {
                double totalConsumption = consumptionService.calculateTotalCarbonImpactForUser(user.getId());
                System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Total Consumption: " + totalConsumption);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving or sorting users: " + e.getMessage());
        }
    }


    private void deleteUser() {
        while (true) {
            listAllUsers();
            System.out.print("Enter the User ID to delete (UUID format): ");
            String userIdString = scanner.nextLine();
            try {
                UUID userId = UUID.fromString(userIdString);
                Optional<User> optionalUser = userService.findById(userId);
                if (optionalUser.isEmpty()) {
                    System.out.println("User not found with the given ID. Please try again.");
                    continue;
                }
                List<Consumption> consumptions = consumptionService.findConsumptionsByUserId(userId);
                if (consumptions != null) {
                    for (Consumption consumption : consumptions) {
                        try {
                            consumptionService.deleteConsumption(consumption.getId());
                            System.out.println("Deleted consumption record with ID: " + consumption.getId());
                        } catch (ConsumptionNotFoundException e) {
                            System.out.println("Failed to delete consumption record with ID: " + consumption.getId() + ". " + e.getMessage());
                        }
                    }
                }
                userService.delete(userId);
                System.out.println("User and their carbon consumption records have been deleted successfully.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            } catch (SQLException e) {
                System.out.println("Database error occurred: " + e.getMessage());
            }
        }
    }


    private void modifyUser() {
        listAllUsers();

        UUID userID = null;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter the User ID to modify (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                validID = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }

        Optional<User> optionalUser = userService.findById(userID);
        if (optionalUser.isEmpty()) {
            System.out.println("User not found.");
            return;
        }

        User user = optionalUser.get(); // Get the User object from Optional
        System.out.println("Current user details: " + user);
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("What would you like to modify?");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Cancel");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    userService.update(userID, newName, null); // Update the name, keep age unchanged
                    System.out.println("Name updated successfully.");
                    validChoice = true;
                    break;
                case 2:
                    System.out.print("Enter new age: ");
                    int newAge;
                    try {
                        newAge = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        userService.update(userID, null, newAge); // Update the age, keep name unchanged
                        System.out.println("Age updated successfully.");
                        validChoice = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // Clear the buffer
                    }
                    break;
                case 3:
                    System.out.println("Modification canceled.");
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void generateReport() {

        listAllUsers();

        UUID userID;
        while (true) {
            System.out.print("Enter user ID to generate report (UUID format): ");
            try {
                userID = UUID.fromString(scanner.nextLine());
                // Check if the user exists
                if (userService.findById(userID).isEmpty()) {
                    System.out.println("User not found. Please enter a valid user ID.");
                } else {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please enter a valid UUID.");
            }
        }

        String reportType;
        LocalDate reportDate;

        while (true) {
            System.out.print("Enter report type (daily, weekly, monthly): ");
            reportType = scanner.nextLine().toLowerCase();
            if (reportType.equals("daily") || reportType.equals("weekly") || reportType.equals("monthly")) {
                break;
            } else {
                System.out.println("Invalid report type. Please enter 'daily', 'weekly', or 'monthly'.");
            }
        }

        while (true) {
            System.out.print("Enter the date for the report (YYYY-MM-DD): ");
            try {
                reportDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        try {
            reportService.generateReport(userID, reportType, reportDate);
        } catch (SQLException e) {
            System.out.println("Error generating report: " + e.getMessage());
        } catch (ConsumptionNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }




}
