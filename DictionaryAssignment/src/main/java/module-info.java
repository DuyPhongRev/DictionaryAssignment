module app.DictionaryFX {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jlayer;
    requires java.sql;
    requires javafx.web;
    requires tess4j;
    requires org.json;
    requires AnimateFX;
    requires java.net.http;
    requires java.desktop;
    requires com.google.gson;

    opens app to javafx.fxml;
    exports app;
    opens app.controllers to javafx.fxml;
}