module org.example.kviz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.example.kviz to javafx.fxml;
    exports org.example.kviz;
}