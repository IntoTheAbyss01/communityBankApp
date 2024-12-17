import java.util.*;

// Main Class
public class CommunityBankApp {

    // Simulated user database
    private static Map<String, String> userCredentials = new HashMap<>(); // Username -> Password
    private static Map<String, List<Account>> userAccounts = new HashMap<>(); // Username -> List of Accounts
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeDummyData(); // Load test data
        System.out.println("=== Welcome to Community Bank ===");

        // Login
        String username = login();

        // Dashboard
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Account Dashboard ---");
            System.out.println("1. View Accounts");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayAccounts(username);
                    break;
                case 2:
                    transferMoney(username);
                    break;
                case 3:
                    withdrawMoney(username);
                    break;
                case 4:
                    System.out.println("Thank you for using Community Bank!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Simulate data setup
    private static void initializeDummyData() {
        // Dummy user credentials
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");

        // Accounts for users
        List<Account> user1Accounts = Arrays.asList(
            new Account("Community Account", 1500.0, 1000.0), // Community Account
            new Account("Client Account", 500.0, 1000.0) // Client Account
        );

        List<Account> user2Accounts = Arrays.asList(
            new Account("Small Business Account", 2000.0, 1000.0) // Small Business Account
        );

        userAccounts.put("user1", user1Accounts);
        userAccounts.put("user2", user2Accounts);
    }

    // Login Method
    private static String login() {
        while (true) {
            System.out.print("Enter Username: ");
            String username = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                System.out.println("Login successful!\n");
                return username;
            } else {
                System.out.println("Invalid credentials. Please try again.\n");
            }
        }
    }

    // Display all accounts for the logged-in user
    private static void displayAccounts(String username) {
        System.out.println("\n--- Your Accounts ---");
        List<Account> accounts = userAccounts.get(username);
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
    }

    // Transfer money between two accounts
    private static void transferMoney(String username) {
        List<Account> accounts = userAccounts.get(username);
        System.out.println("\n--- Transfer Money ---");
        displayAccounts(username);

        System.out.print("Select account to transfer FROM (number): ");
        int fromIndex = scanner.nextInt() - 1;

        System.out.print("Select account to transfer TO (number): ");
        int toIndex = scanner.nextInt() - 1;

        if (fromIndex >= 0 && fromIndex < accounts.size() && toIndex >= 0 && toIndex < accounts.size()) {
            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();

            Account fromAccount = accounts.get(fromIndex);
            Account toAccount = accounts.get(toIndex);

            if (fromAccount.withdraw(amount)) {
                toAccount.deposit(amount);
                System.out.println("Transfer successful!");
            } else {
                System.out.println("Insufficient funds to complete the transfer.");
            }
        } else {
            System.out.println("Invalid account selection.");
        }
    }

    // Withdraw money from an account
    private static void withdrawMoney(String username) {
        List<Account> accounts = userAccounts.get(username);
        System.out.println("\n--- Withdraw Money ---");
        displayAccounts(username);

        System.out.print("Select account to withdraw FROM (number): ");
        int index = scanner.nextInt() - 1;

        if (index >= 0 && index < accounts.size()) {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();

            if (accounts.get(index).withdraw(amount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient funds. Check your balance and overdraft limit.");
            }
        } else {
            System.out.println("Invalid account selection.");
        }
    }
}

// Account Class
class Account {
    private String accountType;
    private double balance;
    private final double overdraftLimit;

    public Account(String accountType, double balance, double overdraftLimit) {
        this.accountType = accountType;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
    }

    // Deposit money into the account
    public void deposit(double amount) {
        balance += amount;
    }

    // Withdraw money with overdraft limit check
    public boolean withdraw(double amount) {
        if ((balance - amount) >= -overdraftLimit) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return accountType + " | Balance: $" + balance + " | Overdraft Limit: $" + overdraftLimit;
    }
}
