package net.leo.message.server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.reply.Reply;

public class Client {

	private Socket sc;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Client(Socket sc) throws IOException {
		this.sc = sc;
		this.oos = new ObjectOutputStream(sc.getOutputStream());
		this.ois = new ObjectInputStream(sc.getInputStream());
	}

	public void command(Command command) throws IOException {
		oos.writeObject(command);
		oos.flush();
	}

	public Reply reply() throws IOException {
		while (true) {
			try {
				return (Reply) ois.readObject();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void close(){
		try {
			oos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ois.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			sc.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
