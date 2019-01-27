package com.jokerconf.banking.server.protocol;

/**
 * Provides common API for all banking protocols implemented by the application server
 * 
 * @author Martin
 */
public interface BankingProtocol {
	
	public String getName();
	
	public Object execute(byte[] packet);
}
