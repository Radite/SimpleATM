import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class User {
    // Class attributes to hold user account details
    private String accountNumber;
    private int pin;
    private double balance;
    private int secureToken; // No longer final as it can be set upon account creation or loaded from file

    /**
     * Constructor for the User class.
     * If a secureToken is provided, it uses that; otherwise, it generates a new one.
     */
    public User(String accountNumber, int pin, double balance, Integer secureToken) {
        this.accountNumber = accountNumber; // User's account number
        this.pin = pin; // User's PIN code
        this.balance = balance; // User's account balance
        // Use the provided secureToken if it's not null, otherwise generate a new one
        this.secureToken = (secureToken != null) ? secureToken : generateSecureToken();
    }

    // Getter for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter for PIN
    public int getPin() {
        return pin;
    }

    // Setter for PIN, allows updating the PIN
    public void setPin(int pin) {
        this.pin = pin;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    // Setter for balance, allows updating the balance
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    /**
     * Private method to generate a secure token.
     * This ensures secure tokens are always 6 digits.
     */
    private static int generateSecureToken() {
        Random rand = new Random();
        // Generate a random number between 100000 (inclusive) and 1000000 (exclusive)
        return rand.nextInt(900000) + 100000;
    }

    // Getter for the secure token
    public int getSecureToken() {
        return secureToken;
    }

    // Method to verify if the provided token matches the user's secure token
    public boolean verifySecureToken(int inputToken) {
        // Simple equality check to verify tokens match
        return this.secureToken == inputToken;
    }

    /**
     * Method to save the user's details to a file.
     * The filename is derived from the user's account number.
     */
    public void saveToFile() {
        String filename = this.accountNumber + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write user details in a structured form to the file
            writer.write("Account Number: " + this.accountNumber);
            writer.newLine();
            writer.write("PIN: " + this.pin);
            writer.newLine();
            writer.write("Balance: " + this.balance);
            writer.newLine();
            writer.write("Secure Token: " + this.secureToken);
        } catch (IOException e) {
            // Log an error if file writing fails
            System.out.println("An error occurred while saving user details to " + filename);
            e.printStackTrace();
        }
    }
}
