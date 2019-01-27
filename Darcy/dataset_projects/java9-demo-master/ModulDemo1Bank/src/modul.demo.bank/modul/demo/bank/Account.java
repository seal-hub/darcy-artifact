package modul.demo.bank;

public class Account {

	private int balance;
	
	public void deposit(int amount) {
		balance += amount;
		System.out.println("Deposit: +" + amount + " - Balance: " + balance);
	}
	
	public boolean withdraw(int amount) {
		if (balance - amount < 0) {
			return false;
		}
		balance -=amount;
		System.out.println("Withdrown: -" + amount + " - Balance: " + balance);
		return true;
	}
	
	public int getBalance() {
		return balance;
	}
}
