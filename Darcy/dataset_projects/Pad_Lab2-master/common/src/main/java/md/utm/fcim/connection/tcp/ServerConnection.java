package md.utm.fcim.connection.tcp;


import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;

import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public interface ServerConnection {

    void write(MessageDto message);

    ObjectInputStream getObjectInputStream();

    Map<Long, List<UserDto>> getMapOfUsers();

    String getUsers();
}