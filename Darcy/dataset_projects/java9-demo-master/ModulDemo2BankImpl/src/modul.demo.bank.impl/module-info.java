module modul.demo.bank.impl {
	requires modul.demo.bank;
	exports modul.demo.bank.impl;
	provides modul.demo.bank.IAccountProvider 
			with modul.demo.bank.impl.AccountProvider;
}