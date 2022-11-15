module com.example.osassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.osassignment to javafx.fxml;
    exports com.example.osassignment;
}