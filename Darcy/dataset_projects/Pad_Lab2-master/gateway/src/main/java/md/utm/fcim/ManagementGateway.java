package md.utm.fcim;

import md.utm.fcim.connection.tcp.impl.CreateConnection;
import md.utm.fcim.connection.tcp.impl.ClientConnectionImpl;
import md.utm.fcim.connection.udp.UdpTool;
import md.utm.fcim.constant.NodeType;
import md.utm.fcim.constant.Utils;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.NodeDto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManagementGateway {

    private ServerSocket serverSocket;

    ManagementGateway(Integer port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket accept = serverSocket.accept();

                ClientConnectionImpl clientConnection = new ClientConnectionImpl(accept);
                MessageDto messageFromClient = clientConnection.receiverMessage();
                messageFromClient.setNodeType(NodeType.MAIN);

                NodeDto nodeWithMaxDependencies = findNodeWithMaxDependencies();

                clientConnection.sendToClient(CreateConnection.getINSTANCE()
                        .build(messageFromClient, nodeWithMaxDependencies.getHost(), nodeWithMaxDependencies.getPort())
                        .getServerConnection()
                        .getUsers());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<NodeDto> findNodesWithUdpConnection() {
        List<NodeDto> nodes = new ArrayList<>();
        try {
            UdpTool udp = new UdpTool();
            nodes = udp.getNodesByMulticast(Utils.IP_GROUP, Utils.PORT_GROUP, "GET ALL");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    private NodeDto findNodeWithMaxDependencies() {
        List<NodeDto> nodes = findNodesWithUdpConnection();
        return nodes
                .stream()
                .filter(node -> node.getDependencies() != null)
                .max(Comparator.comparing(node -> node.getDependencies().size())).get();
    }
}
