package sample.plugin1.impl;

import sample.plugin.Plugin;

/**
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/5
 */
public interface PluginProvider {
	static Plugin provider() {
		return new WebViewPlugin();
	}
}
