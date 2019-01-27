package sample.plugin1.impl;

import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import sample.plugin.Plugin;

/**
 * 网页插件
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/4
 */
public class WebViewPlugin implements Plugin {
    private String name;

    public WebViewPlugin() {
        this.name = "WebViewPlugin";
    }

    @Override
    public String getPluginName() {
        return this.name;
    }

    @Override
    public Tab getPluginTab() {
        Tab tab = new Tab();
        tab.setText(getPluginName());

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("https://www.zhihu.com/people/li-jia-ming-70/posts");

        tab.setContent(browser);
        return tab;
    }
}
