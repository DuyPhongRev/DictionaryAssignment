module com.example.dictionaryassignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.dictionaryassignment to javafx.fxml;
    exports com.example.dictionaryassignment;
}