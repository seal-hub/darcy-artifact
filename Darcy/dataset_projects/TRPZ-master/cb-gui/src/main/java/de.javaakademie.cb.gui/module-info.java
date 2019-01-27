module de.javaakademie.cb.gui {
	exports de.javaakademie.cb.gui to javafx.graphics,javafx.fxml;
	opens de.javaakademie.cb.gui to javafx.fxml;
    requires de.javaakademie.cb.api;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    uses de.javaakademie.cb.api.ConferenceService;    
}