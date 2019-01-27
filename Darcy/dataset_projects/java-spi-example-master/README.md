# Java 9 模块3: SPI模式

标签（空格分隔）： Java

---

SPI(Service Provider Interfaces)模式是java不修改源码进行扩展的方式之一。客户端只和某个接口交互，扩展时只需要将实现了该接口的Service Provider添加到classpath中，客户端即可自动发现新代码并使用。

# 四个角色

 1. Service：提供了某种功能的接口。
 2. Service Loader：负责发现和加载classpath中所有的Service实现。
 3. Client：使用ServiceLoader获取到的接口。
 4. Serivce Provider：提供特定service接口的实现。

# Java SPI开发步骤：

 1. 定义功能service接口。
 2. 开发使用接口的客户端，让客户端使用java.util.ServiceLoader获取接口实现并使用。
 3. 编写Serivce Provider，即特定接口的实现。
 4. 将接口实现构建jar包，添加特殊的meta信息，表示这个jar包是接口的实现。
 5. 将上面的jar包添加到客户端的classpath中，并启动客户端。

# 实战
Java9之前，添加meta信息的方式是通过jar包中 META_INF/services目录下的配置文件进行的。Java9开始，即可以使用以前的方式，还可以在Module Descriptor中使用 uses <interface> 和provides <interface> with <impletion> 进行添加。

下面通过一个javaFX程序进行说明。
实现后的效果如下：
![此处输入图片的描述][1]

![此处输入图片的描述][2]

![此处输入图片的描述][3]

javaFX本身通过client.jar启动，第一个tab页同样来自client.jar。
第二个tab页来自plugin.jar，第三个tab页来自plugin1.jar。不需要后两个tab页时，只需要把对应的jar包从classpath中删除即可。
 
# Java9之前
新建4个maven工程：api、client、plugin1、plugin2.
其中client、plugin1、plugin2都依赖于api。
如下图
![此处输入图片的描述][4]

## api
此工程主要定义service接口，插件都必须实现该接口，client也只和这个接口交互，client并不知道有多少种具体实现。
```java
public interface Plugin {
    String getPluginName();
    Tab getPluginTab();
}
```

## client
此工程是javaFX主工程，sample.Main是启动类，sample.plugin.impl.HelloWorldPlugin是默认的service实现。
Main中使用ServiceLoader加载了classpath中所有的Plugin实现。
```java
private void loadPlugins(TabPane tabPane) {
		//搜索插件
		ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
		//遍历插件并添加到tab视图中
		for (Plugin plugin : loader) {
			tabPane.getTabs().add(plugin.getPluginTab());
		}
	}
```
## Plugin1和Plugin2
都是Plugin接口的具体实现，主要关注的是META-INF/services下的配置文件：
配置文件的名字和实现的接口必须完全相同，此处必须为sample.plugin.Plugin，内容则是具体的接口实现的完全限定名，分别为sample.plugin.impl.ChartPlugin和sample.plugin.impl.WebViewPlugin。如图：
![此处输入图片的描述][5]

分别使用mvn package对每个工程进行构建生成jar包，再把jar包拷贝到lib目录下，运行 
java -cp "lib/*" sample.Main 加载所有插件并运行app

java -cp lib/api-1.0-SNAPSHOT.jar:lib/Client-1.0-SNAPSHOT.jar:lib/Plugin1-1.0-SNAPSHOT.jar sample.Main 只加载默认插件和chart插件并运行app

java -cp lib/api-1.0-SNAPSHOT.jar:lib/Client-1.0-SNAPSHOT.jar:lib/Plugin1-2.0-SNAPSHOT.jar sample.Main 只加载默认插件和webview插件并运行app

注意观察加载不同插件时，app的不同效果。

# Java9开始
Java9开始，除了可以继续使用之前的方式开发SPI，还可以通过模块的方式。
新建4个Java模块工程：api、client、plugin1、plugin2。
Java模块基本开发请参考：[使用Eclipse编写Java9模块hello world][6] 和 [使用命令行编译和运行Java9模块hello world][7]

复制之前的代码，由于Java模块系统不允许不同的模块中包含相同的包，所以需要修改包名，使每个模块下的包都唯一。

## api
该模块被其他所以模块使用，且该模块依赖的javaFX模块，应传递依赖给其他模块，所以api模块的module-info.java内容如下：
```java
module sample.api {
    exports sample.plugin;
    requires transitive javafx.controls;
}
```

## client
该模块使用了api模块中的Plugin接口，依赖于api和javaFX，同时需要将Main类暴露出来，并且使用了ServiceLoader搜索接口，所以client模块的module-info.java内容如下：
```java
module sample.client {
    requires sample.api;
    requires javafx.graphics;
    exports sample.client;
    uses sample.plugin.Plugin; //指定可以load的接口
}
```
使用uses说明模块可以load的接口。

## Plugin0
该模块需要说明自己提供了Plugin的实现，所以module-info.java内容如下：
```java
import sample.plugin0.impl.ChartPlugin;

module sample.plugin0 {
    requires sample.api;
    requires javafx.graphics;
    exports sample.plugin0.impl;
    provides sample.plugin.Plugin with ChartPlugin;
}
```
使用 provides sample.plugin.Plugin with ChartPlugin; 指定该模块可以提供Plugin接口的实现 ChartPlugin。

## Plugin1
Java9之前，Service  Provider必须是实现了service接口的具体的类。而Java9开始，Service Provider不必是具体类，而可以是接口，只是这个接口必须具有一个名为provide的static方法，并且该方法必须返回实现特定service接口的类的实例。

如下PluginProvider接口使用静态provider方法返回了一个WebViewPlugin实例。
```java
public interface PluginProvider {
	static Plugin provider() {
		return new WebViewPlugin();
	}
}
```
无论Service Provider是具体类还是接口，module-info.java中指定方式都是相同的：
```java
module sample.plugin1 {
    requires sample.api;
    requires javafx.graphics;
    requires javafx.web;
    exports sample.plugin1.impl;
    provides sample.plugin.Plugin with PluginProvider;
}
```

## 构建、运行
由于当前maven还不支持Java9模块，所以构建和运行比较繁琐。
### api
javac -encoding "UTF-8" -d mod --module-source-path . sample.api/module-info.java sample.api/sample/plugin/Plugin.java
jar --create --file=../lib/sample.api.jar -C mod/sample.api .

### client
javac -encoding "UTF-8" -d mod --module-path ../api/mod  --module-source-path . sample.client/module-info.java sample.client/sample/client/Main.java sample.client/sample/client/impl/HelloWorldPlugin.java
jar --create --file=../lib/sample.client.jar -C mod/sample.client .

### plugin0
javac -encoding "UTF-8" -d mod --module-path ../api/mod  --module-source-path . sample.plugin0/module-info.java sample.plugin0/sample/plugin0/impl/ChartPlugin.java
jar --create --file=../lib/sample.plugin.jar -C mod/sample.plugin0 .

### plugin1
javac -encoding "UTF-8" -d mod --module-path ../api/mod  --module-source-path . sample.plugin1/module-info.java sample.plugin1/sample/plugin1/impl/PluginProvider.java sample.plugin1/sample/plugin1/impl/WebViewPlugin.java
jar --create --file=../lib/sample.plugin1.jar -C mod/sample.plugin1 .

### 运行
java --module-path lib --module sample.client/sample.client.Main
--module-path不支持指定单个jar，所以增加模块需要将模块jar包添加到lib目录，删除模块需要从lib中移除模块jar包。

完整代码：
https://github.com/pkpk1234/java-spi-example


  [1]: https://raw.githubusercontent.com/pkpk1234/images/master/java9spi/1.jpg
  [2]: https://raw.githubusercontent.com/pkpk1234/images/master/java9spi/2.jpg
  [3]: https://raw.githubusercontent.com/pkpk1234/images/master/java9spi/3.jpg
  [4]: https://raw.githubusercontent.com/pkpk1234/images/master/java9spi/4.jpg
  [5]: https://raw.githubusercontent.com/pkpk1234/images/master/java9spi/5.jpg
  [6]: https://zhuanlan.zhihu.com/p/30743052
  [7]: https://zhuanlan.zhihu.com/p/30762184
