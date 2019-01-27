module application.demo {
	requires java.logging;
	requires banking.server;
	provides com.jokerconf.banking.server.applications.BankingApplication with com.jokerconf.banking.app.demo.DemoApplication;
}
