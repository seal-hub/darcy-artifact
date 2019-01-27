import sample.plugin1.impl.PluginProvider;

module sample.plugin1 {
    requires sample.api;
    requires javafx.graphics;
    requires javafx.web;
    exports sample.plugin1.impl;
    provides sample.plugin.Plugin with PluginProvider;
}