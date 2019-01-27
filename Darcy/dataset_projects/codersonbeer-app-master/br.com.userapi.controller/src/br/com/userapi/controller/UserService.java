package br.com.userapi.controller;

import br.com.userapi.data.UserDao;

import java.util.List;
import java.util.ServiceLoader;

public class UserService {
    public List<String> getUsers() {
        return ServiceLoader.load(UserDao.class).findFirst().get().findNames();
//        ServiceLoader.load(UserDao.class).forEach(s -> {
//            s.findNames().forEach(System.out::println);
//        });
//        return null;
    }
}