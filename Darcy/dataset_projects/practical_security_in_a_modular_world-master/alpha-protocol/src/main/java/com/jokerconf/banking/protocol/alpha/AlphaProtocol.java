package com.jokerconf.banking.protocol.alpha;

import java.util.logging.Logger;

import com.jokerconf.banking.server.protocol.BankingProtocol;

public class AlphaProtocol implements BankingProtocol {

	private static final Logger LOGGER = Logger.getLogger(AlphaProtocol.class.getName());
	
	public String getName() {
		return "Alpha";
	}
	
	public Object execute(byte[] packet) {
		
		LOGGER.info("Sending alpha protocol packet ... ");
		// Logic to execute the alpha packet ... 
		
		return null;
	}
	
}
