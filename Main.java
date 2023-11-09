import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create an instance of the ATM class to handle ATM operations.
        ATM atm = new ATM();
        // Initialize Scanner to read input from the console.
        Scanner scanner = new Scanner(System.in);

        // Loop indefinitely to allow the user to perform multiple actions.
        while (true) {
            // Display the main menu options to the user.
            System.out.println("\nWelcome to the ATM System");
            System.out.println("1. Set up a new account");
            System.out.println("2. Log in to existing account");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            // Capture the user's menu choice as an integer.
            int choice = scanner.nextInt();
            // Clear the scanner's newline character to prepare for next input.
            scanner.nextLine();

            // Evaluate the user's choice and execute corresponding action.
            switch (choice) {
                case 1:
                    // Option 1: Set up a new account.
                    atm.setupAccount();
                    break;
                case 2:
                    // Option 2: Attempt to log in to an existing account.
                    // If login is successful, display the account menu.
                    if (atm.login()) {
                        atm.showMenu();
                    }
                    break;
                case 3:
                    // Option 3: Exit the ATM system.
                    System.out.println("Exiting the ATM system. Thank you for using our service.");
                    // Close the scanner to prevent resource leaks.
                    scanner.close();
                    // Exit the while loop and end the program.
                    return;
                default:
                    // Handle any input that does not match menu options.
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
