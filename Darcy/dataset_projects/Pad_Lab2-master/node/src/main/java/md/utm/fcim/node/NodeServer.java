package md.utm.fcim.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.utm.fcim.connection.tcp.ClientConnection;
import md.utm.fcim.connection.tcp.impl.CreateConnection;
import md.utm.fcim.connection.tcp.impl.ClientConnectionImpl;
import md.utm.fcim.constant.NodeType;
import md.utm.fcim.constant.Utils;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.NodeDto;
import md.utm.fcim.dto.UserDto;
import md.utm.fcim.service.UserService;
import md.utm.fcim.service.impl.UserServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NodeServer extends Thread {

    private ServerSocket serverSocket;
    private UserService userService;
    private NodeDto nodeDto;
    private Map<Long, List<UserDto>> users = new HashMap<>();

    NodeServer(NodeDto nodeDto) {
        this.userService = new UserServiceImpl();
        this.nodeDto = nodeDto;
        try {
            serverSocket = new ServerSocket(nodeDto.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " + this.nodeDto.getPort() + "...");
                Socket accept = serverSocket.accept();

                new Thread(() -> {
                    ClientConnectionImpl clientConnection = new ClientConnectionImpl(accept);
                    MessageDto messageFromGateway = clientConnection.receiverMessage();

                    Utils.dependencyNodes.add(nodeDto.getId());

                    users = getUserFromDependencies(messageFromGateway);
                    users.entrySet().forEach(System.out::println);
                    users.put(nodeDto.getId(), nodeDto.getUsers());

                    handlerUsers(messageFromGateway, clientConnection);
                    users.clear();
                }).start();

            } catch (IOException e) {
                System.out.println("Client was disconnected");
            }
        }
    }

    private Map<Long, List<UserDto>> getUserFromDependencies(MessageDto message) {
        MessageDto messageDto = new MessageDto(message.getMethod(), message.getField(), message.getOperation(), message.getValue());
        messageDto.setNodeType(NodeType.CHILDREN);
        Map<Long, List<UserDto>> users = new HashMap<>();
        if (nodeDto.getDependencies() != null) {
            nodeDto.getDependencies().forEach(dependencyNode -> {
                if (Utils.dependencyNodes.stream().noneMatch(id -> id.equals(dependencyNode.getId()))) {
                    users.putAll(CreateConnection.getINSTANCE()
                            .build(messageDto, dependencyNode.getHost(), dependencyNode.getPort()).getServerConnection().getMapOfUsers());
                }
            });
        }
        return users;
    }

    private void handlerUsers(MessageDto message, ClientConnection clientConnection) {
        if (message.getNodeType().equals(NodeType.MAIN)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                clientConnection.sendToClient(mapper.writeValueAsString(userService
                        .withUsers(users.values()
                                .stream()
                                .flatMap(List::stream)
                                .collect(Collectors.toList()))
                        .methodReference(message)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            clientConnection.sendToNode(users);
        }
    }
}
