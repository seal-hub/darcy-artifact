package md.utm.fcim.connection.udp;

import md.utm.fcim.dto.NodeDto;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class UdpTool {

    public List<NodeDto> getNodesByMulticast(String group, int port, String message) throws IOException {

        MulticastSocket s = new MulticastSocket();
        List<NodeDto> nodes = new ArrayList<>();

        InetAddress IPAddress = InetAddress.getByName(group);

        byte[] sendData;
        byte[] receiveData = new byte[10000];

        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        s.send(sendPacket);
        try {
            s.setSoTimeout(5000);

            while (s.getSoTimeout() > 0) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                s.receive(receivePacket);

                nodes.add(SerializationUtils.deserialize(receivePacket.getData()));
            }
            s.close();
        } catch (Exception e) {
            System.out.println("Soket time out for receive");
        }
        return nodes;
    }

    public DatagramPacket receiveMulticast(String group, int port) throws IOException {
        MulticastSocket s = new MulticastSocket(port);
        s.joinGroup(InetAddress.getByName(group));

        byte[] receiveData = new byte[1000];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        s.receive(receivePacket);
        String sentence = new String(receivePacket.getData(), "UTF-8");
        System.out.println("RECEIVED: " + sentence);
        s.close();
        return receivePacket;

    }

    public void sendResponseToClient(NodeDto nodeDto, DatagramPacket receivePacket, int port, String group) throws IOException {
        MulticastSocket s;

        s = new MulticastSocket(port);

        s.joinGroup(InetAddress.getByName(group));
        InetAddress IPAddress = receivePacket.getAddress();
        int port1 = receivePacket.getPort();

        byte[] data = SerializationUtils.serialize(nodeDto);

        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port1);
        s.send(sendPacket);
        s.close();
    }

}
