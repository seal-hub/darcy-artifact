package com.ymmihw.core.java9.main;

import com.ymmihw.core.java9.modules.hello.HelloInterface;
import com.ymmihw.core.java9.modules.hello.HelloModules;

public class MainApp {
  public static void main(String[] args) {
    HelloModules.doSomething();

    HelloInterface module = new HelloModules();
    module.sayHello();
  }
}
