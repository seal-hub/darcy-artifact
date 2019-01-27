package modul.demo.bank.impl;

import modul.demo.bank.IAccount;
import modul.demo.bank.IAccountProvider;

public class AccountProvider extends IAccountProvider {

	public IAccount getAccount(int number) {
		return new Account(number);
	};
}
