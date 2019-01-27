package modul.demo.bank.impl;

import modul.demo.bank.IAccount;

public class Account extends IAccount {

	private int number;
	private int balance;

	public Account(int number) {
		this.number = number;
	}

	public void deposit(int amount) {
		balance += amount;
		System.out.println("Deposit: +" + amount + " - Balance: " + balance);
	}

	public boolean withdraw(int amount) {
		if (balance - amount < 0) {
			return false;
		}
		balance -= amount;
		System.out.println("Withdrown: -" + amount + " - Balance: " + balance);
		return true;
	}

	public int getBalance() {
		return balance;
	}
	
	public int getNumber() {
		return number;
	}
}
