import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BankInterface extends Remote {
    boolean login(String acc, String pwd) throws RemoteException;

    boolean register(String acc, String pwd) throws RemoteException;

    double checkBalance(String acc) throws RemoteException;

    void deposit(String acc, double amount) throws RemoteException;

    void withdraw(String acc, double amount) throws RemoteException;

    List<String> getTransactionHistory(String acc) throws RemoteException;
}
