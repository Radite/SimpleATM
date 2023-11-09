import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        Scanner scanner = new Scanner(System.in); // Initialize the scanner outside the loop

        while (true) {
            System.out.println("\nWelcome to the ATM System");
            System.out.println("1. Set up a new account");
            System.out.println("2. Log in to existing account");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    atm.setupAccount();
                    break;
                case 2:
                    if (atm.login()) {
                        atm.showMenu();
                    }
                    break;
                case 3:
                    System.out.println("Exiting the ATM system. Thank you for using our service.");
                    scanner.close(); // Close the scanner when we're done with it
                    return; // Exit the loop (and the program)
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
