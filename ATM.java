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
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.print("Enter your account number: ");
            String accountNumber = scanner.next();
            File accountFile = new File(accountNumber + ".txt");

            // Check if the account file exists
            if (!accountFile.exists()) {
                System.out.println("Error: Account number does not exist. Please try again.");
                continue; // Continue will restart the loop, prompting for the account number again
            }

            System.out.print("Enter your PIN: ");
            int pin = scanner.nextInt();

            // Since the account file exists, proceed to load the account data
            try (BufferedReader reader = new BufferedReader(new FileReader(accountFile))) {
                String line;
                String fileAccountNumber = null;
                int filePin = 0;
                double balance = 0.0;
                Integer securityCode = null;

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
                            securityCode = Integer.parseInt(parts[1].trim());
                            break;
                    }
                }

                // Check if the account number and PIN match what was loaded
                if (accountNumber.equals(fileAccountNumber) && pin == filePin) {
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
