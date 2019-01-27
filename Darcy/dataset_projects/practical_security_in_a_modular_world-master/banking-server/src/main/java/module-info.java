module banking.server {
	requires java.logging;
	exports com.jokerconf.banking.server.applications;
	exports com.jokerconf.banking.server.protocol;
	exports com.jokerconf.banking.server.protocol.permissions;
	exports com.jokerconf.banking.server.services;
	uses com.jokerconf.banking.server.applications.BankingApplication;
	uses com.jokerconf.banking.server.protocol.BankingProtocol;
}
