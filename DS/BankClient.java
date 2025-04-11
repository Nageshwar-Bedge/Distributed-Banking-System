import java.rmi.Naming;
import java.util.Scanner;

public class BankClient {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            BankInterface bank = (BankInterface) Naming.lookup("rmi://localhost:1099/Bank");
            boolean success = false;

            System.out.println("Welcome to Distributed Bank System");
            System.out.println("1. Login\n2. Register");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            String acc, pwd;
            System.out.print("Enter account number: ");
            acc = sc.nextLine();
            System.out.print("Enter password: ");
            pwd = sc.nextLine();

            if (choice == 1) {
                success = bank.login(acc, pwd);
                if (!success) {
                    System.out.println("Login failed. Invalid credentials.");
                    return;
                }
                System.out.println("Login successful!");
            } else if (choice == 2) {
                success = bank.register(acc, pwd);
                if (success) {
                    System.out.println("Registered successfully. You are now logged in.");
                } else {
                    System.out.println("Account already exists. Please login.");
                    return;
                }
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            // 🔽 Main menu after login or registration
            while (true) {
                System.out.println("\nChoose Operation:");
                System.out.println("1. Check Balance\n2. Deposit\n3. Withdraw\n4. History\n5. Exit");
                System.out.print("Enter option: ");
                int opt = sc.nextInt();

                switch (opt) {
                    case 1:
                        System.out.println("Balance: " + bank.checkBalance(acc));
                        break;
                    case 2:
                        System.out.print("Enter amount to deposit: ");
                        double d = sc.nextDouble();
                        bank.deposit(acc, d);
                        System.out.println("Deposited successfully!");
                        break;
                    case 3:
                        System.out.print("Enter amount to withdraw: ");
                        double w = sc.nextDouble();
                        bank.withdraw(acc, w);
                        System.out.println("Withdrawn if funds were sufficient.");
                        break;
                    case 4:
                        System.out.println("Transaction History:");
                        for (String h : bank.getTransactionHistory(acc)) {
                            System.out.println("- " + h);
                        }
                        break;
                    case 5:
                        System.out.println("Exiting. Thank you!");
                        sc.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
