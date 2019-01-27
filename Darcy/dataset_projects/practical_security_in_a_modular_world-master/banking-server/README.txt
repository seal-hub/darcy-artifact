Demo 1
======

1) what will happen when the applicaton is run ?
(security policy: D:\software\Java\jdk1.8.0_25\jre\lib\security\java.policy)
grant codeBase "file:/D:/stuff/seminars/jokerconf/2017/Practical_security_in_a_modular_world/practical_security_in_a_modular_world/banking-server/target/classes/" {
         permission java.security.AllPermission;
};



2) FIX protocol not found - how to solve it ? 
(wrap code in priveleged action:


		BankingProtocol targetProtocol = AccessController.doPrivileged(new PrivilegedAction<BankingProtocol>() {

			@Override
			public BankingProtocol run() {
				ServiceLoader<BankingProtocol> protocols = ServiceLoader.load(BankingProtocol.class);
				BankingProtocol targetProtocol = null;
				for(BankingProtocol protocol : protocols) {
					if(protocolName.equals(protocol.getName())) {
						targetProtocol = protocol;
						break;
					}
				}
				
				return targetProtocol;
			}
		});
)

3) specify protocol permission in security policy
grant codeBase "file:/D:/stuff/seminars/jokerconf/2017/Practical_security_in_a_modular_world/practical_security_in_a_modular_world/banking-server/target/deploy/demo-application-1.0.0-SNAPSHOT.jar" {
		permission com.jokerconf.banking.server.protocol.permissions.ProtocolPermission "FIX";
};


4) show policytool

Demo 2
======

0) comment out security manager

1) show Logger protection domain
Module currentModule = Server.class.getModule();
Module loggerModule = Logger.class.getModule();
		
System.out.println(currentModule.getName());
System.out.println(loggerModule.getName());
System.out.println(Server.class.getProtectionDomain().getCodeSource());
System.out.println(currentModule.getClass().getProtectionDomain().getCodeSource());
System.out.println(loggerModule.getClass().getProtectionDomain().getCodeSource());
System.out.println(Logger.class.getProtectionDomain().getCodeSource());

2) break module encapsulation:
java --add-opens java.base/java.lang=ALL-UNNAMED

System.out.println(loggerModule.isExported("sun.util.logging.internal"));
Field allModules = Module.class.getDeclaredField("EVERYONE_MODULE");
allModules.setAccessible(true);
Method method = Module.class.getDeclaredMethod("implAddExportsOrOpens", String.class, Module.class, boolean.class, boolean.class);
method.setAccessible(true);
method.invoke(loggerModule, "sun.util.logging.internal", allModules.get(Module.class), true, true);
System.out.println(loggerModule.isExported("sun.util.logging.internal"));
LoggingProviderImpl provider = new LoggingProviderImpl();

3) build custom JDK image:
bin\jlink.exe --module-path jmods --add-modules java.logging --output D:/software/Java/jdk9_logging_only

4) encapsulate app server and applications:
create module-info.java for server and applications (in default packages):
module banking.server {
	requires java.logging;
	exports com.jokerconf.banking.server.applications;
	exports com.jokerconf.banking.server.protocol;
	exports com.jokerconf.banking.server.services;
	exports com.jokerconf.banking.server.protocol.permissions;
	uses com.jokerconf.banking.server.applications.BankingApplication;
	uses com.jokerconf.banking.server.protocol.BankingProtocol;
}
module protocol.alpha {
	requires java.logging;
	requires banking.server;
	provides com.jokerconf.banking.server.protocol.BankingProtocol with com.jokerconf.banking.protocol.alpha.AlphaProtocol;
}
module protocol.fix {
	requires java.logging;
	requires banking.server;
	provides com.jokerconf.banking.server.protocol.BankingProtocol with com.jokerconf.banking.protocol.fix.FixProtocol;
}
module application.demo {
	requires java.logging;
	requires banking.server;
	provides com.jokerconf.banking.server.applications.BankingApplication with com.jokerconf.banking.app.demo.DemoApplication;
}

build server module: 
cd D:\stuff\seminars\jokerconf\2017\Practical_security_in_a_modular_world\practical_security_in_a_modular_world\
set PATH=D:\software\Java\jdk9_181\bin;%PATH%
mkdir modules\banking.server modules\protocol.fix modules\protocol.alpha modules\application.demo

dir /s /B banking-server\src\main\java\com\jokerconf\*.java > sources.txt
javac -d modules\banking.server banking-server\src\main\java\module-info.java @sources.txt
java --module-path modules -m banking.server/com.jokerconf.banking.server.Server

dir /s /B fix-protocol\src\main\java\com\jokerconf\*.java > sources.txt
javac --module-path modules -d modules\fix.protocol fix-protocol\src\main\java\module-info.java @sources.txt

dir /s /B alpha-protocol\src\main\java\com\jokerconf\*.java > sources.txt
javac --module-path modules -d modules\alpha.protocol alpha-protocol\src\main\java\module-info.java @sources.txt

dir /s /B demo-application\src\main\java\com\jokerconf\*.java > sources.txt
javac --module-path modules -d modules\demo.application demo-application\src\main\java\module-info.java @sources.txt

5) uncomment security manager

6) comment security manager and dump code base

7) add the java 9 security policy:
grant codeBase "file:/D:/stuff/seminars/jokerconf/2017/Practical_security_in_a_modular_world/practical_security_in_a_modular_world/modules/demo.application" {
		permission com.jokerconf.banking.server.protocol.permissions.ProtocolPermission "FIX";
};

grant codeBase "file:/D:/stuff/seminars/jokerconf/2017/Practical_security_in_a_modular_world/practical_security_in_a_modular_world/modules/banking.server" {
		permission com.jokerconf.banking.server.protocol.permissions.ProtocolPermission "FIX";
};

