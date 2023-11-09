import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        
        System.out.println("Welcome to the ATM System");
        System.out.println("1. Set up a new account");
        System.out.println("2. Log in to existing account");
        System.out.print("Please choose an option: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                atm.setupAccount();
                break;
            case 2:
                if (atm.login()) {
                    atm.showMenu();
                }
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }

        scanner.close();
    }
}
