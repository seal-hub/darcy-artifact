package md.utm.fcim.connection.tcp.impl;

import md.utm.fcim.connection.tcp.ServerConnection;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerConnectionImpl implements ServerConnection {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    ServerConnectionImpl(Socket socket) {
        try {
            this.socket = socket;
            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(MessageDto messageDto) {
        try {
            objectOutputStream.writeObject(messageDto);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Long, List<UserDto>> getMapOfUsers() {
        try {
            return (Map<Long, List<UserDto>>) getObjectInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUsers() {
        try {
            return (String) getObjectInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }
}
