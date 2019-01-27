package net.leo.message.client.domain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application extends JFrame {

	JPanel root;

	public Application() {

		root = new JPanel(new BorderLayout());
		root.setPreferredSize(new Dimension(1200, 900));

		add(root);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		new Thread(() -> {
			run();
		}).start();
	}

	private void run() {
		try {
			Socket sc = new Socket("127.0.0.1", 12345);
			new Interpreter(root, sc);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Application();
		new Application();
		new Application();
	}
}
