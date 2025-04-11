import java.rmi.Naming;

public class ServerMain {
    public static void main(String[] args) {
        try {
            BankServer server = new BankServer();
            Naming.rebind("rmi://localhost:1099/Bank", server);
            System.out.println("Bank Server is running on rmi://localhost:1099/Bank...");
        } catch (Exception e) {
            System.out.println("Server error: " + e);
        }
    }
}
