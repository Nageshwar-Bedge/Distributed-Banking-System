import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;

public class BankServer extends UnicastRemoteObject implements BankInterface {
    private Map<String, String> accounts;
    private Map<String, Double> balances;
    private Map<String, List<String>> history;

    public BankServer() throws RemoteException {
        accounts = loadData("accounts.ser");
        balances = loadData("balances.ser");
        history = loadData("history.ser");

        if (accounts == null)
            accounts = new HashMap<>();
        if (balances == null)
            balances = new HashMap<>();
        if (history == null)
            history = new HashMap<>();
    }

    public boolean login(String acc, String pwd) throws RemoteException {
        return accounts.containsKey(acc) && accounts.get(acc).equals(pwd);
    }

    public boolean register(String acc, String pwd) throws RemoteException {
        if (accounts.containsKey(acc))
            return false;
        accounts.put(acc, pwd);
        balances.put(acc, 0.0);
        List<String> logs = new ArrayList<>();
        logs.add(new Date() + " Account created.");
        history.put(acc, logs);

        saveData("accounts.ser", accounts);
        saveData("balances.ser", balances);
        saveData("history.ser", history);
        return true;
    }

    public double checkBalance(String acc) throws RemoteException {
        return balances.getOrDefault(acc, 0.0);
    }

    public void deposit(String acc, double amount) throws RemoteException {
        double current = balances.getOrDefault(acc, 0.0);
        balances.put(acc, current + amount);
        history.get(acc).add(new Date() + " Deposited: " + amount);

        saveData("balances.ser", balances);
        saveData("history.ser", history);
    }

    public void withdraw(String acc, double amount) throws RemoteException {
        double current = balances.getOrDefault(acc, 0.0);
        if (amount > current) {
            history.get(acc).add(new Date() + " Withdrawal failed (Insufficient funds)");
        } else {
            balances.put(acc, current - amount);
            history.get(acc).add(new Date() + " Withdrawn: " + amount);
        }

        saveData("balances.ser", balances);
        saveData("history.ser", history);
    }

    public List<String> getTransactionHistory(String acc) throws RemoteException {
        return history.getOrDefault(acc, new ArrayList<>());
    }

    // Serialization helpers
    @SuppressWarnings("unchecked")
    private <T> T loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    private void saveData(String filename, Object data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (Exception e) {
            System.out.println("Error saving " + filename + ": " + e.getMessage());
        }
    }
}
