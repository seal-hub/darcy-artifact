package br.com.userapi.main;

import br.com.userapi.controller.UserService;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {

      new UserService().getUsers().stream().forEach(System.out::println);

//        new UserDao(){
//
//            @Override
//            public List<String> findNames() {
//                return null;
//            }
//        }
//        new UserService().getUsers().stream().forEach(u -> System.out.println(StringUtils.abbreviate(u, 12)));


//            Method m = UserChild.class.getDeclaredMethod("prnt");
//            m.setAccessible(true);
//            m.invoke(null); // works Fine
    }

}
