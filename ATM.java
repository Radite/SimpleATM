import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class ATM {
    // Scanner to read user input and Random to generate random numbers or tokens
    private Scanner scanner;
    private Random rand;

    // Account to represent the user's account, userMap to track logged-in users by account number
    private Account account;
    private Map<String, User> userMap;
    private String currentAccountNumber; // Keep track of the currently logged-in user's account number

    // Constructor initializes the scanner, the random object, and the userMap
    public ATM() {
        this.scanner = new Scanner(System.in);
        this.rand = new Random();
        this.userMap = new HashMap<>();
    }

    // Method for user login. It allows for multiple attempts until a successful login
    public boolean login() {
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.print("Enter your account number: ");
            String accountNumber = scanner.next();
            File accountFile = new File(accountNumber + ".txt");

            // Check if an account file exists, otherwise prompt again
            if (!accountFile.exists()) {
                System.out.println("Error: Account number does not exist. Please try again.");
                continue;
            }

            System.out.print("Enter your PIN: ");
            int pin = scanner.nextInt();

            // Read account details from file and validate login credentials
            try (BufferedReader reader = new BufferedReader(new FileReader(accountFile))) {
                // Variables to hold account details read from file
                String line;
                String fileAccountNumber = null;
                int filePin = 0;
                double balance = 0.0;
                Integer securityCode = null;

                // Parse the account file to extract account details
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(": ");
                    // Switch case to assign values to variables based on the prefix
                    switch (parts[0]) {
                        case "Account Number": fileAccountNumber = parts[1].trim(); break;
                        case "PIN": filePin = Integer.parseInt(parts[1].trim()); break;
                        case "Balance": balance = Double.parseDouble(parts[1].trim()); break;
                        case "Secure Token": securityCode = Integer.parseInt(parts[1].trim()); break;
                    }
                }

                // Validate the account number and PIN
                if (accountNumber.equals(fileAccountNumber) && pin == filePin) {
                    // If valid, create User object, update userMap and set currentAccountNumber
                    User user = new User(fileAccountNumber, filePin, balance, securityCode);
                    userMap.put(accountNumber, user);
                    this.currentAccountNumber = accountNumber;
                    System.out.println("Login successful.");
                    loginSuccessful = true;
                } else {
                    System.out.println("Login failed. Incorrect PIN. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while trying to login.");
                e.printStackTrace();
            }
        }
        return loginSuccessful;
    }

    // Method to setup a new account with unique account number and 4-digit PIN
    public void setupAccount() {
        // Variables for account number and file object
        String accountNumber;
        File file;

        // Loop to ensure the generated account number is unique
        while (true) {
            accountNumber = String.valueOf(100000 + rand.nextInt(900000));
            file = new File(accountNumber + ".txt");

            // Break the loop if the generated account number doesn't already have an account file
            if (!file.exists()) {
                break;
            }
        }

        // Loop to ensure user enters a valid 4-digit PIN
        int pin = 0;
        while (true) {
            System.out.print("Enter a new 4-Digit PIN: ");
            pin = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left-over
            if (String.valueOf(pin).length() == 4) {
                break;
            } else {
                System.out.println("Invalid PIN. The PIN must be exactly 4 digits.");
            }
        }

        // Create a new User object with the account number and PIN
        User newUser = new User(accountNumber, pin, 0.0, null);
        newUser.saveToFile(); // Save user details to file

        // Inform user of account creation and provide secure token
        System.out.println("Account created successfully. Account Number: " + accountNumber);
        System.out.println("Store your Secure Token safely: " + newUser.getSecureToken());
    }

    // Method to display a menu of actions to the user
    public void showMenu() {
        // Retrieve the logged-in user's details
        User loggedInUser = userMap.get(currentAccountNumber);

        // If no user is logged in, exit the method
        if (loggedInUser == null) {
            System.out.println("Login session expired or invalid.");
            return;
        }
        
        // Associate the Account object with the logged-in User
        this.account = new Account(loggedInUser);

        // Loop to display the menu and process user actions
        while (true) {
            System.out.println("\nWelcome to the ATM. Please choose an operation:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();

            // Switch case to handle the chosen operation
            switch (choice) {
                case 1: performBalanceInquiry(); break;
                case 2: performDeposit(); break;
                case 3: performWithdrawal(); break;
                case 4: performPinChange(); break;
                case 5: System.out.println("Thank you for using the ATM. Goodbye!"); return;
                default: System.out.println("Invalid option. Please try again."); break;
            }
        }
    }

    private void performBalanceInquiry() {
        System.out.println("Your current balance is: " + account.checkBalance());
    }

private void performDeposit() {
    double amount;
    while (true) {
        System.out.print("Enter amount to deposit: ");
        try {
            amount = scanner.nextDouble();
            if (amount <= 0) {
                System.out.println("The amount must be a positive number.");
                continue;
            }
            break; // Exit loop if a valid amount is entered
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. You must enter a number.");
            scanner.next(); // Clear the invalid input
        }
    }
    account.deposit(amount);
}

private void performWithdrawal() {
    double amount;
    while (true) {
        System.out.print("Enter amount to withdraw: ");
        try {
            amount = scanner.nextDouble();
            if (amount <= 0) {
                System.out.println("The amount must be a positive number.");
                continue; // Continue will skip the rest of the loop and start over
            }
            // If the amount is positive, try to perform the withdrawal
            if (account.withdraw(amount)) {
                System.out.println("Withdrawal successful.");
                break; // Break out of the loop if the withdrawal is successful
            } else {
                System.out.println("Withdrawal failed. Insufficient funds or error.");
                break; // Break out of the loop if there's an error or insufficient funds
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. You must enter a number.");
            scanner.next(); // Clear the scanner buffer
        }
    }
}
    

private void performPinChange() {
    int oldPin = 0, newPin = 0;
    boolean validOldPin = false, validNewPin = false;
    
    // Validate old PIN
    while (!validOldPin) {
        System.out.print("Enter old PIN: ");
        try {
            oldPin = scanner.nextInt();
            if (String.valueOf(oldPin).length() == 4) {
                validOldPin = true;
            } else {
                System.out.println("The old PIN must be a 4-digit number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. You must enter numbers only.");
            scanner.next(); // Clear the scanner buffer
        }
    }

    // Validate new PIN
    while (!validNewPin) {
        System.out.print("Enter new PIN: ");
        try {
            newPin = scanner.nextInt();
            if (String.valueOf(newPin).length() == 4) {
                if (newPin != oldPin) {
                    validNewPin = true;
                } else {
                    System.out.println("The new PIN cannot be the same as the old PIN.");
                }
            } else {
                System.out.println("The new PIN must be a 4-digit number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. You must enter numbers only.");
            scanner.next(); // Clear the scanner buffer
        }
    }

    // Assuming you have a method to prompt for and validate the secure token
    int secureToken = promptForSecureToken();

    // Change PIN if old PIN and secure token are correct
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

private int promptForSecureToken() {
    int secureToken;
    System.out.print("Enter secure token: ");
    while (true) {
        try {
            secureToken = scanner.nextInt();
            break; // Exit loop if input is an integer
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. The secure token must be numbers only.");
            scanner.next(); // Clear the scanner buffer
        }
    }
    return secureToken;
}
}
