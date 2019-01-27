package md.utm.fcim.service;

import md.utm.fcim.constant.FieldName;
import md.utm.fcim.constant.OperationType;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;
import md.utm.fcim.service.impl.UserServiceImpl;

import java.util.List;

public interface UserService {

    List<UserDto> methodReference(MessageDto message);

    List<UserDto> findAll(FieldName field, OperationType operation);

    List<UserDto> sorted(FieldName field, OperationType operation);

    UserServiceImpl withUsers(List<UserDto> users);
}
