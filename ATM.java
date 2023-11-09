import java.util.Scanner;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Scanner scanner;
    private Account account; // This needs to be used after a successful login
    private Map<String, User> userMap; // To hold user data after login

    public ATM() {
        this.scanner = new Scanner(System.in);
        this.userMap = new HashMap<>();
    }

    // Log-in functionality
    public boolean login() {
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();

        // Attempt to retrieve the user information from a file
        try (BufferedReader reader = new BufferedReader(new FileReader(accountNumber + ".txt"))) {
            String line;
            String fileAccountNumber = null;
            int filePin = 0;
            double balance = 0.0;
            int securityCode = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                switch (parts[0]) {
                    case "Account Number":
                        fileAccountNumber = parts[1].trim();
                        break;
                    case "PIN":
                        filePin = Integer.parseInt(parts[1].trim());
                        break;
                    case "Balance":
                        balance = Double.parseDouble(parts[1].trim());
                        break;
                    case "Security Code":
                        securityCode = Integer.parseInt(parts[1].trim());
                        break;
                }
            }

            // Check if account number and PIN match
            if (accountNumber.equals(fileAccountNumber) && pin == filePin) {
                // User authenticated, create User object and store it in map
                User user = new User(accountNumber, pin, balance);
                userMap.put(accountNumber, user);
                System.out.println("Login successful.");
                return true;
            } else {
                System.out.println("Login failed. Incorrect account number or PIN.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while trying to login.");
            e.printStackTrace();
            return false;
        }
    }

    public void start() {
        if (login()) { // If login is successful, proceed to show the menu
            showMenu();
        }
    }

    public void setupAccount() {
        System.out.print("Enter a new account number or press Enter to generate one: ");
        String accountNumberInput = scanner.nextLine();
        String accountNumber = accountNumberInput.isEmpty() ? UUID.randomUUID().toString() : accountNumberInput;

        System.out.print("Enter a new PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left-over

        User newUser = new User(accountNumber, pin, 0.0); // Start with a balance of 0.0
        newUser.saveToFile();

        System.out.println("Account created successfully. Account Number: " + accountNumber);
        // The account number will be needed for login, so it should be stored securely and provided to the user.
    }


    private void showMenu() {
        System.out.println("Please enter your account number: ");
        String accountNumber = scanner.next();
        User loggedInUser = userMap.get(accountNumber);

        if (loggedInUser == null) {
            System.out.println("Invalid account number or PIN.");
            return;
        }
        
        this.account = new Account(loggedInUser); // Set the account for the logged in user

        while (true) {
            System.out.println("\nWelcome to the ATM. Please choose an operation:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    performBalanceInquiry();
                    break;
                case 2:
                    performDeposit();
                    break;
                case 3:
                    performWithdrawal();
                    break;
                case 4:
                    performPinChange();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

private void performBalanceInquiry() {
        System.out.println("Your current balance is: " + account.checkBalance());
    }

    private void performDeposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        account.deposit(amount); // Call deposit on the account instance
    }

    private void performWithdrawal() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private void performPinChange() {
        System.out.print("Enter old PIN: ");
        int oldPin = scanner.nextInt();
        System.out.print("Enter new PIN: ");
        int newPin = scanner.nextInt();
        System.out.print("Enter secure token: "); // You need to handle secure token
        int secureToken = scanner.nextInt();

        if (account.changePin(oldPin, newPin, secureToken)) {
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Failed to change PIN.");
        }
    }

}