module sample.client {
    requires sample.api;
    requires javafx.graphics;
    exports sample.client;
    uses sample.plugin.Plugin;
}