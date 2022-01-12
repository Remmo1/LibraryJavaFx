module com.example.javafxcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.example.javafxcrud to javafx.fxml;
    exports com.example.javafxcrud;
}