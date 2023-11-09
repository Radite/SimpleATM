public class Account {
    private User user; // Define user as a member variable

    public User getUser(){
        return user;
    }

    public Account(User user) {
        this.user = user;
    }

    public double checkBalance() {
        return user.getBalance(); // Call getBalance on the user instance
    }

    public int checkPin(){
        return user.getPin();
    }

    public int checkSecureToken(){
        return user.getSecureToken();
    }
    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);
            user.saveToFile();
            System.out.println("Amount deposited successfully. New balance is: " + newBalance);
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0) {
            if (user.getBalance() >= amount) {
                double newBalance = user.getBalance() - amount;
                user.setBalance(newBalance);
                user.saveToFile();
                System.out.println("Amount withdrawn successfully. New balance is: " + newBalance);
                return true;
            } else {
                System.out.println("Insufficient funds. Your balance is: " + user.getBalance());
            }
        } else {
            System.out.println("Invalid amount. Please enter a positive value.");
        }
        return false; // This should be at the end to ensure a boolean is always returned
    }

    public boolean changePin(int oldPin, int newPin, int secureToken) {
        // Check if the secure token is correct
        if (!user.verifySecureToken(secureToken)) {
            System.out.println("Invalid security code. PIN change failed.");
            return false; // Exit the method if the secure token is incorrect
        }

        // If the old PIN and the secure token are correct, proceed to change the PIN
        user.setPin(newPin);
        return true;
    }
}