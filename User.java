import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class User {
    private String accountNumber;
    private int pin;
    private double balance;
    private int secureToken; // Make sure this is not final anymore

    // Update the constructor to accept an Integer for the secureToken
    public User(String accountNumber, int pin, double balance, Integer secureToken) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        // If secureToken is null, generate a new one, otherwise use the provided one
        this.secureToken = (secureToken != null) ? secureToken : generateSecureToken();
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public int getPin(){
        return pin;
    }

    public void setPin(int pin){
        this.pin = pin;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }
    
    // This method generates a random 6-digit secure token
    private static int generateSecureToken() {
        Random rand = new Random();
        return rand.nextInt(900000) + 100000;
    }

    public int getSecureToken(){
        return secureToken;
    }

    public boolean verifySecureToken(int secureToken){
        return this.secureToken == secureToken;
    }

    public void saveToFile() {
        String filename = this.accountNumber + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Account Number: " + this.accountNumber);
            writer.newLine();
            writer.write("PIN: " + this.pin); // Make sure this is writing the new PIN
            writer.newLine();
            writer.write("Balance: " + this.balance);
            writer.newLine();
            writer.write("Secure Token: " + this.secureToken);
        } catch (IOException e) {
            System.out.println("An error occurred while saving user details to " + filename);
            e.printStackTrace();
        }
    }
}






