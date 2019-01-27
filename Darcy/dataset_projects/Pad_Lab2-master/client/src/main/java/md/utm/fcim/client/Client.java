package md.utm.fcim.client;

import md.utm.fcim.connection.tcp.impl.CreateConnection;
import md.utm.fcim.constant.FieldName;
import md.utm.fcim.constant.MethodName;
import md.utm.fcim.constant.OperationType;
import md.utm.fcim.dto.MessageDto;

public class Client {
    public static void main(String[] args) {
        CreateConnection connection = CreateConnection.getINSTANCE().build(
                new MessageDto(MethodName.SORTED, FieldName.AGE, OperationType.DESCENDING),
                "localhost",
                5555);
        System.out.println(connection.getServerConnection().getUsers());
    }
}
