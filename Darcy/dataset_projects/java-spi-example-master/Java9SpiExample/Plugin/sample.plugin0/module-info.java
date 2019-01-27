import sample.plugin0.impl.ChartPlugin;

module sample.plugin0 {
    requires sample.api;
    requires javafx.graphics;
    exports sample.plugin0.impl;
    provides sample.plugin.Plugin with ChartPlugin;
}