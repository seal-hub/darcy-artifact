package com.jokerconf.banking.server;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.util.Enumeration;
import java.util.ServiceLoader;

import com.jokerconf.banking.server.applications.BankingApplication;

public class Server {

	public static void main(String[] args) {
		System.setSecurityManager(new SecurityManager());

		// dumpCodeSource();
		// dumpPermissions();
		// dumpClasspath();
		// dumpDemoAppCodeSource();

		ServiceLoader<BankingApplication> apps = ServiceLoader.load(BankingApplication.class);
		for (BankingApplication app : apps) {
			app.execute();
		}

	}

	private static void dumpCodeSource() {
		System.out.println(Server.class.getProtectionDomain().getCodeSource());
	}

	private static void dumpPermissions() {
		PermissionCollection permissionsCollection = Policy.getPolicy()
				.getPermissions(Server.class.getProtectionDomain());
		Enumeration<Permission> permissions = permissionsCollection.elements();
		while (permissions.hasMoreElements()) {
			System.out.println(permissions.nextElement().getName());
		}
	}

	private static void dumpDemoAppCodeSource() {

		try {
			CodeSource source = Class.forName("com.jokerconf.banking.app.demo.DemoApplication").getProtectionDomain()
					.getCodeSource();
			System.out.println(source);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
