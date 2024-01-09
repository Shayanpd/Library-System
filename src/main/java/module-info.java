module com.example.labb2dbt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.controlsfx.controls;


    opens com.example.labb2dbt to javafx.fxml;
    opens com.example.labb2dbt.model to javafx.base;
    exports com.example.labb2dbt;
    exports com.example.labb2dbt.view;
}