package com.jokerconf.banking.server.services;

import java.util.ServiceLoader;

import com.jokerconf.banking.server.protocol.BankingProtocol;
import com.jokerconf.banking.server.protocol.permissions.ProtocolPermission;

public class ProtocolService {
	
	public void sendPacket(byte[] packet, String protocolName) {

		if(protocolName == null) {
			throw new RuntimeException("Protocol name not specified.");
		}
		
		ServiceLoader<BankingProtocol> protocols = ServiceLoader.load(BankingProtocol.class);
		BankingProtocol targetProtocol = null;
		for(BankingProtocol protocol : protocols) {
			if(protocolName.equals(protocol.getName())) {
				targetProtocol = protocol;
				break;
			}
		}
		
		if(targetProtocol == null) {
			throw new RuntimeException(String.format("Target protocol: %s not found", protocolName));
		}
		
		SecurityManager securityManager = System.getSecurityManager();
		if(securityManager != null) {
			securityManager.checkPermission(new ProtocolPermission(protocolName));
		}

		targetProtocol.execute(packet);
	}
}
