package views;

import java.util.InputMismatchException;
import java.util.Scanner;

public class menu {

    private Scanner scanner;

    public menu() {

        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        int choice;
        do {
            System.out.println("\n---     Carbon Footprint Tracker Menu     ---");
            System.out.println("    1. Create New User");
            System.out.println("    2. Add Carbon Consumption to User");
            System.out.println("    3. Display User Details");
            System.out.println("    4. List all users");
            System.out.println("    5. Consultation of reports");
            System.out.println("    6. Modify User");
            System.out.println("    7. Delete User");
            System.out.println("    8. Exit");
            System.out.print("\n    Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:

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

}
