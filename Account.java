public class Account {
    // Member variable to hold a reference to the User object associated with this account
    private User user;

    // Constructor that takes a User object and initializes the account with it
    public Account(User user) {
        this.user = user;
    }

    // Method to retrieve the balance from the User object
    public double checkBalance() {
        // Directly accesses the User object's method to get the balance
        return user.getBalance();
    }

    // Method to retrieve the PIN from the User object
    public int checkPin(){
        // Directly accesses the User object's method to get the PIN
        return user.getPin();
    }

    // Method to retrieve the secure token from the User object
    public int checkSecureToken(){
        // Directly accesses the User object's method to get the secure token
        return user.getSecureToken();
    }

    // Method to deposit a specified amount into the account
    public void deposit(double amount) {
        // Checks if the deposit amount is a positive value
        if (amount > 0) {
            // Calculates the new balance by adding the deposit amount
            double newBalance = user.getBalance() + amount;
            // Sets the new balance in the User object
            user.setBalance(newBalance);
            // Persists the updated balance to the file
            user.saveToFile();
            System.out.println("Amount deposited successfully. New balance is: " + newBalance);
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
    }

    // Method to withdraw a specified amount from the account
    public boolean withdraw(double amount) {
        // Checks if the withdrawal amount is a positive value
        if (amount > 0) {
            // Checks if the current balance is sufficient for the withdrawal
            if (user.getBalance() >= amount) {
                // Calculates the new balance by subtracting the withdrawal amount
                double newBalance = user.getBalance() - amount;
                // Sets the new balance in the User object
                user.setBalance(newBalance);
                // Persists the updated balance to the file
                user.saveToFile();
                System.out.println("Amount withdrawn successfully. New balance is: " + newBalance);
                return true; // Indicates the withdrawal was successful
            } else {
                System.out.println("Insufficient funds. Your balance is: " + user.getBalance());
            }
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
        return false; // Indicates the withdrawal was unsuccessful
    }

    // Method to change the PIN of the account
    public boolean changePin(int oldPin, int newPin, int secureToken) {
        // Verifies the secure token to authorize the PIN change
        if (!user.verifySecureToken(secureToken)) {
            System.out.println("Invalid security code. PIN change failed.");
            return false; // If the secure token is incorrect, the method exits and indicates failure
        }

        // If the secure token is correct, the PIN is changed in the User object
        user.setPin(newPin);
        // Persists the new PIN to the file
        user.saveToFile();
        return true; // Indicates the PIN change was successful
    }
}
