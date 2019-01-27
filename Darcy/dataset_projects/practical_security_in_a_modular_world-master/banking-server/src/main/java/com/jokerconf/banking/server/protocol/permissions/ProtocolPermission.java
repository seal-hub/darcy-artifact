package com.jokerconf.banking.server.protocol.permissions;

import java.security.BasicPermission;

/**
 * A permission to check whether the use of a certain banking protocol is allowed or not.
 * 
 * @author Martin
 */
public class ProtocolPermission extends BasicPermission {

	private static final long serialVersionUID = 2290445416327623283L;
	
	public ProtocolPermission(String name) {
		super(name);
	}
	
}
