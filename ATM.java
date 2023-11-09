import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ATM {
    private Scanner scanner;
    private Account account; // This needs to be used after a successful login
    private Map<String, User> userMap; // To hold user data after login
    private String currentAccountNumber; // New field to keep track of the logged-in user's account number
    private Random rand;

    public ATM() {
        this.scanner = new Scanner(System.in);
        this.userMap = new HashMap<>();
        this.rand = new Random();
    }

    // Log-in functionality
    public boolean login() {
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();

        Integer securityCode = null;  // Initialize securityCode as null

        // Attempt to retrieve the user information from a file
        try (BufferedReader reader = new BufferedReader(new FileReader(accountNumber + ".txt"))) {
            String line;
            String fileAccountNumber = null;
            int filePin = 0;
            double balance = 0.0;

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
                    case "Secure Token":
                        securityCode = Integer.parseInt(parts[1].trim());  // Parse and store the secure token
                        break;
                }
            }

            // Now check if the account number and PIN match what was loaded
            if (accountNumber.equals(fileAccountNumber) && pin == filePin) {
                // Create a User object with the loaded data, including the secureToken
                User user = new User(fileAccountNumber, filePin, balance, securityCode);
                userMap.put(accountNumber, user);  // Store the user in the map
                this.currentAccountNumber = accountNumber; // Set the current account number
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

    public void setupAccount() {
        String accountNumber;
        File file;

        while (true) {
            accountNumber = String.valueOf(100000 + rand.nextInt(900000));
            file = new File(accountNumber + ".txt");

            // If file does not exist, we've found a unique account number
            if (!file.exists()) {
                break;
            }
            // If the file exists, the loop will continue and generate a new number
        }
        int pin = 0;
        while (true){
        System.out.print("Enter a new 4-Digit PIN: ");
        pin = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left-over
        if (String.valueOf(pin).length() == 4) {
            break;
        } else {
            System.out.println("Invalid PIN. The PIN must be exactly 4 digits.");
        }
    }

        // Create a new User with the unique account number and 4-digit PIN

        User newUser = new User(accountNumber, pin, 0.0, null); // Start with a balance of 0.0
        newUser.saveToFile();

        System.out.println("Account created successfully. Account Number: " + accountNumber);
        System.out.println("Store your Secure Token safely: " + newUser.getSecureToken());

    }


    public void showMenu() {
        User loggedInUser = userMap.get(currentAccountNumber);

        if (loggedInUser == null) {
            System.out.println("Login session expired or invalid.");
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
    if (oldPin != account.checkPin()) {
        System.out.println("Incorrect Pin");
        return;
        
    }
    System.out.print("Enter new PIN: ");
    int newPin = scanner.nextInt();
    if (oldPin == newPin){
        System.out.println("Old pin and new pin cannot be the same.");
        return;
    }
    System.out.print("Enter secure token: ");
    int secureToken = scanner.nextInt();

    User currentUser = userMap.get(currentAccountNumber);
    if (currentUser != null && currentUser.verifySecureToken(secureToken)) {
        if (currentUser.getPin() == oldPin) { 
            currentUser.setPin(newPin); // Update the PIN in the User object
            currentUser.saveToFile(); // Save the User object to file
            userMap.put(currentAccountNumber, currentUser); // Update the map with the new User object
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect old PIN.");
        }
    } else {
        System.out.println("Invalid security code or user not found.");
    }
}
}