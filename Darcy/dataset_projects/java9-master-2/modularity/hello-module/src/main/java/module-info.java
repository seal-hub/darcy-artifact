module hello.modules {
  exports com.ymmihw.core.java9.modules.hello;

  provides com.ymmihw.core.java9.modules.hello.HelloInterface
      with com.ymmihw.core.java9.modules.hello.HelloModules;
}
