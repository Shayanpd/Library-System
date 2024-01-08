package com.example.labb2dbt.view;

import com.example.labb2dbt.model.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.control.Alert.AlertType.*;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author anderslm@kth.se
 */
public class Controller {

    private final BooksPane booksView; // view
    BooksDbInterface booksDb; // model

    public Controller(BooksDbInterface booksDb, BooksPane booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        if (searchFor != null && !searchFor.isEmpty()) {
            Runnable searchTask = () -> {
                try {
                    List<Book> result = null;
                    switch (mode) {
                        case Title:
                            result = booksDb.searchBooksByTitle(searchFor);
                            break;
                        case ISBN:
                            result = booksDb.searchBooksByISBN(searchFor);
                            break;
                        case Author:
                            result = booksDb.searchBooksByAuthor(searchFor);
                            break;
                        case Genre:
                            result = booksDb.searchBooksByGenre(searchFor);
                            break;
                        case Rating:
                            result = booksDb.searchBooksByRating(searchFor);
                            break;
                        default:
                            result = new ArrayList<>();
                    }
                    // Update UI with the result
                    List<Book> finalResult = result;
                    Platform.runLater(() -> {
                        if (finalResult == null || finalResult.isEmpty()) {
                            booksView.showAlertAndWait(
                                    "No results found.", INFORMATION);
                        } else {
                            booksView.displayBooks(finalResult);
                        }
                    });
                } catch (Exception e) {
                    // Handle exceptions
                    Platform.runLater(() -> {
                        booksView.showAlertAndWait("Database error.", ERROR);
                    });
                }
            };

            Thread searchThread = new Thread(searchTask);
            searchThread.start();
        } else {
            booksView.showAlertAndWait(
                    "Enter a search string!", WARNING);
        }
    }

    public boolean connectToDatabase() throws BooksDbException {
        return booksDb.connect("mongodb://localhost:27017", "book_database");
    }
    public void disconnectFromDatabase() throws BooksDbException{
        booksDb.disconnect();
    }
    public void addBook(Book book) throws BooksDbException {
        Runnable addTask = () -> {
            try {
                booksDb.addBook(book);
            } catch (BooksDbException e) {
                handleException(e);
            }
        };

        Thread addThread = new Thread(addTask);
        addThread.start();
    }
    public void addAuthor(Author author) throws BooksDbException {
        Runnable addTask = () -> {
            try {
                booksDb.addAuthor(author);
            } catch (BooksDbException e) {
                handleException(e);
            }
        };

        Thread addThread = new Thread(addTask);
        addThread.start();
    }
    public void deleteBook(Book book) throws BooksDbException {
        Runnable deleteTask = () -> {
            try {
                booksDb.deleteBook(book.getBookId());
            } catch (BooksDbException e) {
                handleException(e);
            }
        };

        Thread deleteThread = new Thread(deleteTask);
        deleteThread.start();
    }

    public void updateBook(Book book) throws BooksDbException {
        Runnable updateTask = () -> {
            try {
                booksDb.updateBook(book);
            } catch (BooksDbException e) {
                handleException(e);
            }
        };

        Thread updateThread = new Thread(updateTask);
        updateThread.start();
    }
    public void exitButtonDisconnect(){
        try {
            booksDb.disconnect();
            System.out.println("disconnected from DB");
        } catch (Exception e) {
            handleException(e);
        }
    }
    private void handleException(Exception e) {
        Platform.runLater(() -> {
            booksView.showAlertAndWait("Error: " + e.getMessage(), ERROR);
        });
    }

    public void updateBookRating(Book book, int newRating) throws BooksDbException {
        booksDb.updateBookRating(book, newRating);
    }

}
