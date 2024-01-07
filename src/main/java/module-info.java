module com.example.labb2dbt {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labb2dbt to javafx.fxml;
    exports com.example.labb2dbt;
}