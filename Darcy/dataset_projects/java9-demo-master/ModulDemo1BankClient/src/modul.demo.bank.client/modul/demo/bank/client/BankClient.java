package modul.demo.bank.client;

import modul.demo.bank.Account;

public class BankClient {

	public static void main(String[] args) {
		Account account = new Account();
		System.out.println("Balance: " + account.getBalance());
		
		account.deposit(100);
		account.withdraw(50);
		account.deposit(100);
		account.withdraw(25);
		account.deposit(100);
		account.withdraw(75);
		account.deposit(100);
		
		System.out.println("Final Balance: " + account.getBalance());
	}
}
