package modul.demo.bank.client;

import java.util.Iterator;
import java.util.ServiceLoader;

import modul.demo.bank.IAccount;
import modul.demo.bank.IAccountProvider;

public class BankClient {

	public static void main(String[] args) throws InterruptedException {
		IAccount account = getAccount(1);
		System.out.println("Balance: " + account.getBalance());
		
		account.deposit(100);
		account.withdraw(50);
		account.deposit(100);
		account.withdraw(25);
		account.deposit(100);
		account.withdraw(75);
		account.deposit(100);
		
		System.out.println("Final Balance: " + account.getBalance());
		Thread.sleep(100000);
	}

	private static IAccount getAccount(int number) {
		ServiceLoader<IAccountProvider> sl = ServiceLoader.load(IAccountProvider.class);
		Iterator<IAccountProvider> iter = sl.iterator();
		if (!iter.hasNext()) {
			throw new RuntimeException("No service providers found!");
		}
		IAccountProvider provider = iter.next();
		return provider.getAccount(number);
	}

}
