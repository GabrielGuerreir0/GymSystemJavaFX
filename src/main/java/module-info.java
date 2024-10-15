module ifce.gymsystemjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    // Abre o pacote Controller para o JavaFX
    opens Controller to javafx.fxml;

    // Abre o pacote Model para o JavaFX
    opens Model to javafx.base;

    // Abre o pacote principal
    opens ifce.gymsystemjavafx to javafx.fxml;

    exports ifce.gymsystemjavafx;
}
