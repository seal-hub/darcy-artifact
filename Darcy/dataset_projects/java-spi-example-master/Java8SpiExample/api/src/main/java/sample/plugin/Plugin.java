package sample.plugin;

import javafx.scene.control.Tab;

/**
 * 插件接口
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/4
 */
public interface Plugin {
    String getPluginName();
    Tab getPluginTab();
}
