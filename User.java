import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

class User {
    private String accountNumber;
    private int pin;
    private double balance;
    private final int secureToken;

    public User(String accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.secureToken = generateSecureToken(); // Generate token on construction
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
    private int generateSecureToken() {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.accountNumber + ".txt"))) {
            writer.write("Account Number: " + this.accountNumber);
            writer.newLine();
            writer.write("PIN: " + this.pin);
            writer.newLine();
            writer.write("Balance: " + this.balance);
            writer.newLine();
            writer.write("Secure Token: " + this.secureToken); // Corrected to secureToken
        } catch (IOException e) {
            System.out.println("An error occurred while saving user details.");
            e.printStackTrace();
        }
    }
}





