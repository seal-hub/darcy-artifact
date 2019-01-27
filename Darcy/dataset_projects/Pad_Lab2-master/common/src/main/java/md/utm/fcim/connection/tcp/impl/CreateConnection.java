package md.utm.fcim.connection.tcp.impl;

import md.utm.fcim.connection.tcp.ServerConnection;
import md.utm.fcim.dto.MessageDto;

import java.io.IOException;
import java.net.Socket;

public class CreateConnection {

    private static CreateConnection INSTANCE;
    private Socket socket;
    private ServerConnection serverConnection;

    public static CreateConnection getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new CreateConnection();
        }
        return INSTANCE;
    }

    public CreateConnection build(MessageDto messageDto, String host, Integer port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverConnection = new ServerConnectionImpl(socket);
        serverConnection.write(messageDto);
        return this;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
