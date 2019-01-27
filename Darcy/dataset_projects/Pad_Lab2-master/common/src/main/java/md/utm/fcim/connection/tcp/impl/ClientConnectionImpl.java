package md.utm.fcim.connection.tcp.impl;

import md.utm.fcim.connection.tcp.ClientConnection;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.currentThread;

public class ClientConnectionImpl implements ClientConnection {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientConnectionImpl(Socket socket) {
        try {
            this.socket = socket;
            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToNode(Map<Long, List<UserDto>> userDtos) {
        try {
            getObjectOutputStream().writeObject(userDtos);
            getObjectOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        closeConnection();
    }


    public void sendToClient(String json) {
        try {
            getObjectOutputStream().writeObject(json);
            getObjectOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            objectInputStream.close();
            this.socket.close();
            currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageDto receiverMessage() {
        MessageDto messageDto = null;
        try {
            messageDto = (MessageDto) getObjectInputStream().readObject();
            System.out.println(messageDto);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messageDto;
    }

    private ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    private ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    @Override
    public String toString() {
        return "ClientConnectionImpl{" +
                "socket=" + socket +
                ", objectOutputStream=" + objectOutputStream +
                ", objectInputStream=" + objectInputStream +
                '}';
    }
}
