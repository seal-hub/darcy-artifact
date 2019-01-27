package net.leo.message.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import net.leo.message.server.game.Game;

public class Main {

	public Main() throws IOException {

		ServerSocket ss = new ServerSocket(12345);

		List<Client> clientList = new ArrayList<>(3);
		for (int i = 0 ; i < 3 ; i++) {
			clientList.add(new Client(ss.accept()));
		}
		new Game(clientList);
	}

	public static void main(String[] args) throws IOException {

		new Main();
	}
}
