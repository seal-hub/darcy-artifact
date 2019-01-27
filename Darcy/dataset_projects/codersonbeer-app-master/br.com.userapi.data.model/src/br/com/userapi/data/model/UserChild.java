package br.com.userapi.data.model;

public class UserChild {

    public Integer age = 4;

    public UserChild(){

    }
    public UserChild(String name, int age){
        this.age = age;
    }

    public String getUser(){
        return "Name: " + "\nage: " + age;
    }


    private static void prnt(){
        System.out.println("print");
    }
}
