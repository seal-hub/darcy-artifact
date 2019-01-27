package com.jokerconf.banking.app.demo;

import java.util.logging.Logger;

import com.jokerconf.banking.server.applications.BankingApplication;
import com.jokerconf.banking.server.services.ProtocolService;

public class DemoApplication implements BankingApplication {

	private static final Logger LOGGER = Logger.getLogger(DemoApplication.class.getName());

	public void execute() {
		LOGGER.info("Executing demo application ...");
		String packet = "...";
		ProtocolService service = new ProtocolService();
		service.sendPacket(packet.getBytes(), "FIX");
	}
	
}
