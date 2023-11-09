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

    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);
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

    public boolean changePin(int oldPin, int newPin, int secureToken) { // Added secureToken as a parameter
        if (user.getPin() == oldPin) {
            if (user.verifySecureToken(secureToken)) {
                user.setPin(newPin);
                System.out.println("PIN changed successfully.");
                return true;
            } else {
                System.out.println("Invalid security code. PIN change failed.");
                return false;
            }
        } else {
            System.out.println("Incorrect PIN. Please try again.");
            return false;
        }
    }
}
