module protocol.fix {
	requires java.logging;
	requires banking.server;
	provides com.jokerconf.banking.server.protocol.BankingProtocol with com.jokerconf.banking.protocol.fix.FixProtocol;
}
