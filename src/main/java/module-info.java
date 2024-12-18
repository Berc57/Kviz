module org.example.kviz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.kviz to javafx.fxml;
    exports org.example.kviz;
}