package com.example.labb2dbt;

import com.example.labb2dbt.model.BooksDbException;
import com.example.labb2dbt.model.BooksDbImpl;
import com.example.labb2dbt.view.BooksPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application start up.
 *
 * @author anderslm@kth.se
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws BooksDbException {

        BooksDbImpl booksDb = new BooksDbImpl(); // model

        BooksPane root = new BooksPane(booksDb);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
                System.out.println("disconnected from DB");
            } catch (Exception e) {}
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
