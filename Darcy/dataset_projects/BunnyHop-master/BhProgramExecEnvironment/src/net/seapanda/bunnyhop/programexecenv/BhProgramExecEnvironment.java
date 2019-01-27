/**
 * Copyright 2017 K.Koike
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.seapanda.bunnyhop.programexecenv;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

import net.seapanda.bunnyhop.bhprogram.common.BhProgramHandler;
import net.seapanda.bunnyhop.bhprogram.common.LocalClientSocketFactory;
import net.seapanda.bunnyhop.bhprogram.common.RemoteClientSocketFactory;
/**
 * @author K.Koike
 */
public class BhProgramExecEnvironment {

	public static void main(String[] args) {

		boolean local = true;
		if (args.length >= 1)
			local = args[0].equals("true");

		LogManager.INSTANCE.init();

		try {
			BhProgramHandlerImpl programHandler = new BhProgramHandlerImpl();
			programHandler.init();
			Remote remote =
				UnicastRemoteObject.exportObject(
					programHandler,
					0,
					local ? new LocalClientSocketFactory(0) : new RemoteClientSocketFactory(0),
					local ? new LocalServerSocketFactory(0) : new RemoteServerSocketFactory(0));
			RMIServerSocketFactory socketFactory = local ? new LocalServerSocketFactory(1) : new RemoteServerSocketFactory(1);
			Registry registry =
				LocateRegistry.createRegistry(
					0,
					RMISocketFactory.getDefaultSocketFactory(),
					socketFactory);
			registry.rebind(BhProgramHandler.class.getSimpleName(), remote);

			if (socketFactory instanceof LocalServerSocketFactory) {
				System.out.println("\n" + ((LocalServerSocketFactory)socketFactory).getLocalPort() + BhParams.BhProgram.RIM_TCP_PORT_SUFFIX);	//don't remove
			}
			else {
				System.out.println("\n" + ((RemoteServerSocketFactory)socketFactory).getLocalPort() + BhParams.BhProgram.RIM_TCP_PORT_SUFFIX);	//don't remove
			}
		}
		catch(Exception e){
			System.out.println("\n" + "null" + BhParams.BhProgram.RIM_TCP_PORT_SUFFIX);	//don't remove
			LogManager.INSTANCE.msgForDebug("main " + e.toString());
		}
	}

}
