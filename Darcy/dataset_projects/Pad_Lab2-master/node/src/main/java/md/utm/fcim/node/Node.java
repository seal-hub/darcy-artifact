package md.utm.fcim.node;

import md.utm.fcim.connection.udp.UdpTool;
import md.utm.fcim.constant.Utils;
import md.utm.fcim.dto.NodeDto;

import java.io.IOException;
import java.net.DatagramPacket;

public class Node {

    private NodeDto nodeDto;

    public Node(NodeDto nodeDto) {
        this.nodeDto = nodeDto;
        System.out.println(nodeDto);
        this.run();
    }

    private void run() {
        new NodeServer(nodeDto).start();

        UdpTool udp = new UdpTool();
        DatagramPacket receivePacket;

        try {
            while (true) {
                receivePacket = udp.receiveMulticast(Utils.IP_GROUP, Utils.PORT_GROUP);

                System.out.println("udp" + this.nodeDto);
                udp.sendResponseToClient(this.nodeDto, receivePacket, Utils.PORT_GROUP, Utils.IP_GROUP);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


}
