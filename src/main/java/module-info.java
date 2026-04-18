module org.example.tiktaktoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.tiktaktoe to javafx.fxml;
    exports org.example.tiktaktoe;
}