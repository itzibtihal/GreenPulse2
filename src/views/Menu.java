package views;

import entities.User;
import services.ConsumptionService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Menu {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final Scanner scanner;
    private final UserService userService;
    private final ConsumptionService consumptionService;

    public Menu(UserService userService, ConsumptionService consumptionService) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.consumptionService = consumptionService;
    }

    public void displayMainMenu() {
        int choice;
        do {
            System.out.println("\n---     Carbon Footprint Tracker Menu     ---");
            System.out.println("    1. Create New User");
            System.out.println("    2. Add Carbon Consumption to User");
            System.out.println("    3. Display User Details");
            System.out.println("    4. List all users");
            System.out.println("    5. User Data Filtering");
            System.out.println("    6. Calculation of Average Consumption");
            System.out.println("    7. Detection of Inactive Users");
            System.out.println("    8. Sorting Users by Consumption");
            System.out.println("    9. Consultation of reports");
            System.out.println("    10. Modify User");
            System.out.println("    11. Delete User");
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

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:


                        break;
                    case 9:
                            break;
                    case 10:
                                break;
                    case 11:
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


}
