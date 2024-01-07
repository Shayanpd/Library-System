module com.example.labb2dbt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;


    opens com.example.labb2dbt to javafx.fxml;
    exports com.example.labb2dbt;
}