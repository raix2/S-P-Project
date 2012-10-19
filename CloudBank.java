import java.net.*;
import java.io.*;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class CloudBank {
	private class CloudBankAccount {
		/**
		 * User accounts
		 */
		String name;
		double balance;
		
		private CloudBankAccount(String name) {
			this.name = name;
			balance = 0;
		}
		
		private CloudBankAccount(String name, double balance) {
			this.name = name;
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
	}
}
