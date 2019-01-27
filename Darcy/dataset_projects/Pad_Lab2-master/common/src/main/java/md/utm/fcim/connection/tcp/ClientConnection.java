package md.utm.fcim.connection.tcp;

import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface ClientConnection {

    void sendToNode(Map<Long, List<UserDto>> users);

    void sendToClient(String json);

    MessageDto receiverMessage();
}
