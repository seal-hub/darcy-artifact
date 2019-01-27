package com.jokerconf.banking.protocol.fix;

import java.util.logging.Logger;

import com.jokerconf.banking.server.protocol.BankingProtocol;

public class FixProtocol implements BankingProtocol {

	private static final Logger LOGGER = Logger.getLogger(FixProtocol.class.getName());
	
	public String getName() {
		return "FIX";
	}

	public Object execute(byte[] packet) {
		
		LOGGER.info("Sending fix protocol packet ... ");
		// Logic to execute the fix packet ... 
		
		return null;
	}
	
}
