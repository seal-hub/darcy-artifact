package md.utm.fcim.service.impl;

import md.utm.fcim.constant.FieldName;
import md.utm.fcim.constant.OperationType;
import md.utm.fcim.dto.MessageDto;
import md.utm.fcim.dto.UserDto;
import md.utm.fcim.service.UserService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private List<UserDto> users;

    public UserServiceImpl() {
    }

    @Override
    public List<UserDto> methodReference(MessageDto message) {
        List<UserDto> users = new ArrayList<>();
        try {
            Method findAll = UserService.class.getDeclaredMethod(message.getMethod().getName(), FieldName.class, OperationType.class);
            try {
                users = (List<UserDto>) findAll.invoke(this, message.getField(), message.getOperation());
                users.forEach(System.out::println);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<UserDto> findAll(FieldName field, OperationType operation) {
        return users;
    }

    @Override
    public List<UserDto> sorted(FieldName field, OperationType operation) {
        return users
                .stream()
                .sorted(Objects.requireNonNull(buildComparatorForSorted(field, operation)))
                .collect(Collectors.toList());
    }

    public List<UserDto> filter() {
        return users
                .stream()
                .filter(user -> user.getAge() < 22)
                .collect(Collectors.toList());
    }

    private Comparator<? super UserDto> buildComparatorForSorted(FieldName field, OperationType operation) {
        switch (field) {
            case ID:
                return buildComparatorForSorted(UserDto::getId, operation);
            case AGE:
                return buildComparatorForSorted(UserDto::getAge, operation);
            default:
                System.out.println("Command not found");
        }
        return null;
    }

    private Comparator<? super UserDto> buildComparatorForSorted(Function<? super UserDto, ? extends Long> function, OperationType operation) {
        if (operation.equals(OperationType.ASCENDING))
            return Comparator.comparing(function);
        if (operation.equals(OperationType.DESCENDING))
            return Comparator.comparing(function).reversed();
        return null;
    }

    @Override
    public UserServiceImpl withUsers(List<UserDto> users) {
        this.users = users;
        return this;
    }
}
