module giselle.ui {
    requires javafx.fxml;
    requires javafx.controls;
    requires giselle.db;

    exports giselle.ui to javafx.graphics;
    opens giselle.ui to javafx.fxml;
}