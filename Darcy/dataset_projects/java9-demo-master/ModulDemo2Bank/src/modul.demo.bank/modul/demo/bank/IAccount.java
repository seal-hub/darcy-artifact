package modul.demo.bank;

public abstract class IAccount {
	public abstract void deposit(int amount);

	public abstract boolean withdraw(int amount);

	public abstract int getBalance();
}
