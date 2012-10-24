import java.net.*;
import java.io.*;
import java.util.HashMap;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class CloudBank {
	/*
	 * Hold the bank accounts mapped to name
	 */
	HashMap<String, CloudBankAccount> accounts;
	
	public static void main(String[] argv) {
		// Open connection, wait for contact
		// Verify whether user or Cloud provider
		// If user:
		// Valid commands: DEPOSIT, BALANCE
		// If Cloud:
		// Valid commands: TRANSFER
	}
	
	public CloudBank() {
		accounts = new HashMap<String, CloudBankAccount>();
		accounts.put("Cloud", new CloudBankAccount(4000000));
		accounts.put("Finn McMissile", new CloudBankAccount(1100));
		accounts.put("Holly Shiftwell", new CloudBankAccount(3465));
		accounts.put("Tow Mater", new CloudBankAccount(8750));
		accounts.put("Lightning McQueen", new CloudBankAccount(5465));
	}
	
	/**
	 * Checks if requested transfer amount is valid as well as transfers money from user to Cloud Provider
	 * @param name Account to be charged
	 * @param amount to be charged
	 * @return Transaction successful
	 */
	public boolean transferToCloud(String name, double amount) {
		try {
			accounts.get(name).withdraw(amount);
			accounts.get("Cloud").deposit(amount);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public double getBalance(String name) {
		return accounts.get(name).getBalance();
	}
	
	/**
	 * Class for Cloud Bank Accounts
	 * Can withdraw or add money (in Cloud Dollars)
	 */
	
	private class CloudBankAccount {
		/**
		 * User accounts
		 */
		double balance;
		
		private CloudBankAccount() {
			balance = 0;
		}
		
		private CloudBankAccount(double balance) {
			this.balance = balance;
		}
		
		private void deposit(double amount) {
			balance += amount;
		}
		
		private void withdraw(double amount) throws Exception {
			if(amount > balance)
				throw new Exception("Not enough cloud dollars to make transaction.");
			balance -= amount;
		}
		
		private double getBalance() {
			return balance;
		}
	}
}
