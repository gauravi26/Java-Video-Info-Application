module com.example.swing {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.swing to javafx.fxml;
    exports com.example.swing;
}