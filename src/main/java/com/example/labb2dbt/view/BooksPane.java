package com.example.labb2dbt.view;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.labb2dbt.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import java.util.Date;





/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author anderslm@kth.se
 */
public class BooksPane extends VBox {

    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view

    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;

    private MenuBar menuBar;

    private Controller controller;

    public BooksPane(BooksDbImpl booksDb) {
        final Controller controller = new Controller(booksDb, this);
        this.init(controller);
        this.controller = controller;
    }

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    public void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }
    
    /**
     * Notify user on input error or exceptions.
     * 
     * @param msg the message
     * @param type types: INFORMATION, WARNING et c.
     */
    protected void showAlertAndWait(String msg, Alert.AlertType type) {
        // types: INFORMATION, WARNING et c.
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    private void init(Controller controller) {

        booksInTable = FXCollections.observableArrayList();

        // init views and event handlers
        initBooksTable();
        initSearchView(controller);
        initMenus();

        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, searchButton);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(menuBar, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)
        booksTable.setPlaceholder(new Label("No rows to display"));

        // define columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, LocalDate> publishedCol = new TableColumn<>("Published");
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));
        TableColumn<Book, String> storyLine = new TableColumn<>("Story Line");
        TableColumn<Book, Integer> rating = new TableColumn<>("Rating");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        TableColumn<Book, String> genre = new TableColumn<>("Genres");

        booksTable.getColumns().addAll(titleCol, isbnCol, publishedCol, storyLine, rating, genre);
        // give title column some extra space
        titleCol.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.5));

        // define how to fill data for each cell, 
        // get values from Book properties
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));
        storyLine.setCellValueFactory(new PropertyValueFactory<>("storyLine"));
        genre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenresAsString()));

        // associate the table view with the data
        booksTable.setItems(booksInTable);
    }

    private void initSearchView(Controller controller) {
        searchField = new TextField();
        searchField.setPromptText("Search for...");
        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);
        searchButton = new Button("Search");
        
        // event handling (dispatch to controller)

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                executeSearch();
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                executeSearch();
            }
        });
    }
    private void executeSearch(){
        String searchFor = searchField.getText();
        SearchMode mode = searchModeBox.getValue();
        controller.onSearchSelected(searchFor, mode);
    }

    private void initMenus() {

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem connectItem = new MenuItem("Connect to Db");
        MenuItem disconnectItem = new MenuItem("Disconnect");
        fileMenu.getItems().addAll(exitItem, connectItem, disconnectItem);

        // Adding event handlers for file menu items
        exitItem.setOnAction(event -> {
            System.out.println("Exit button pressed");
            controller.exitButtonDisconnect();
            javafx.application.Platform.exit(); // Terminate the application
        });
        connectItem.setOnAction(event -> {
            System.out.println("Connect to Db button pressed");
            try {
                if(controller.connectToDatabase()) System.out.println("Connect successful");
            } catch (BooksDbException e) {
                throw new RuntimeException(e);
            }
        });
        disconnectItem.setOnAction(event -> {
            System.out.println("Disconnect button pressed");
            try {
                controller.disconnectFromDatabase();
                System.out.println("Disconnect successful");
            } catch (BooksDbException e) {
                throw new RuntimeException(e);
            }
        });

        Menu searchMenu = new Menu("Search");
        MenuItem titleItem = new MenuItem("Title");
        MenuItem isbnItem = new MenuItem("ISBN");
        MenuItem authorItem = new MenuItem("Author");
        MenuItem genreItem = new MenuItem("Genre");
        MenuItem ratingItem = new MenuItem("Rating");

        searchMenu.getItems().addAll(titleItem, isbnItem, authorItem, genreItem, ratingItem);

        // Adding event handlers for search menu items
        titleItem.setOnAction(event -> searchModeBox.setValue(SearchMode.Title));
        isbnItem.setOnAction(event -> searchModeBox.setValue(SearchMode.ISBN));
        authorItem.setOnAction(event -> searchModeBox.setValue(SearchMode.Author));
        genreItem.setOnAction(event -> searchModeBox.setValue(SearchMode.Genre));
        ratingItem.setOnAction(event -> searchModeBox.setValue(SearchMode.Rating));

        Menu manageMenu = new Menu("Manage");

        MenuItem removeItem = new MenuItem("Remove");
        MenuItem updateItem = new MenuItem("Update");
        MenuItem setRatingItem = new MenuItem("Change rating");

        Menu addItemMenu = new Menu("Add");

        MenuItem addAuthorItem = new MenuItem("Add Author");
        MenuItem addBookItem = new MenuItem("Add Book");

        manageMenu.getItems().addAll(addItemMenu, removeItem, updateItem, setRatingItem);
        addItemMenu.getItems().addAll(addBookItem, addAuthorItem);

        addBookItem.setOnAction(event -> {
            System.out.println("Add Book button pressed");
            showAddBookDialog();
        });

        addAuthorItem.setOnAction(event -> {
            System.out.println("Add Author button pressed");
            showAddAuthorDialog();
        });
        setRatingItem.setOnAction(event -> {
            System.out.println("Change rating button pressed");
            Optional<Book> selectedBook = showSelectBookDialog();
            selectedBook.ifPresent(this::showChangeRatingDialog);
        });


        removeItem.setOnAction(event -> {
            System.out.println("Remove button pressed");
            showRemoveBookDialog();
        });
        updateItem.setOnAction(event -> {
            System.out.println("Update button pressed");
            Optional<Book> selectedBook = showSelectBookDialog();
            selectedBook.ifPresent(book -> {
                // Proceed with updating the book
                showUpdateBookDialog(book);
            });
        });




        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, searchMenu, manageMenu);
    }



    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setResizable(true);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        DatePicker publishedDatePicker = new DatePicker();
        publishedDatePicker.setPromptText("Published Date");

        CheckListView<Author> authorCheckListView = new CheckListView<>();
        List<Author> existingAuthors = getExistingAuthors();
        authorCheckListView.getItems().addAll(existingAuthors);

        Label authorLabel = new Label("Authors:");
        authorLabel.setTooltip(new Tooltip("Select Authors"));

        TextField genreField = new TextField();
        genreField.setPromptText("Genres (comma-separated)");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("ISBN:"), 0, 1);
        grid.add(isbnField, 1, 1);

        grid.add(new Label("Published Date:"), 0, 2);
        grid.add(publishedDatePicker, 1, 2);

        grid.add(authorLabel, 0, 3);
        grid.add(authorCheckListView, 1, 3);

        grid.add(new Label("Genres:"), 0, 4);
        grid.add(genreField, 1, 4);

        grid.add(new Label("Description:"), 0, 5);
        grid.add(descriptionArea, 1, 5);

        scrollPane.setContent(grid);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                LocalDate publishedDate = publishedDatePicker.getValue();
                Book book = new Book(-1, isbnField.getText(), titleField.getText(), publishedDate);
                book.setStoryLine(descriptionArea.getText());

                List<Author> selectedAuthors = authorCheckListView.getCheckModel().getCheckedItems();
                book.setAuthors(new ArrayList<>(selectedAuthors));

                List<Genre> genres = Arrays.stream(genreField.getText().split("\\s*,\\s*"))
                        .map(Genre::new)
                        .collect(Collectors.toList());
                book.setGenres(new ArrayList<>(genres));

                return book;
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                controller.addBook(book);
            } catch (BooksDbException e) {
                showAlertAndWait("Error: " + e.getMessage(), Alert.AlertType.ERROR);
                System.out.println("error i catch inom showadddialogue");
            }
        });
    }

    private List<Author> getExistingAuthors() {
        try {
            return controller.booksDb.getAllAuthors();
        } catch (BooksDbException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAddAuthorDialog() {
        Dialog<Author> dialog = new Dialog<>();
        dialog.setTitle("Add New Author");

        // Set up the form for adding an author
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Author Name");

        DatePicker dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setPromptText("Date of Birth");

        grid.add(new Label("Author Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Date of Birth:"), 0, 1);
        grid.add(dateOfBirthPicker, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result to an Author object when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Create an Author object with the entered details
                String name = nameField.getText();
                LocalDate dateOfBirth = dateOfBirthPicker.getValue();

                Author author = new Author(name, dateOfBirth);
                // Set additional fields as needed
                return author;
            }
            return null;
        });

        // Show the dialog and capture the result
        Optional<Author> result = dialog.showAndWait();
        result.ifPresent(author -> {

            try {
                controller.addAuthor(author);
            } catch (BooksDbException e) {
                throw new RuntimeException(e);
            }

        });
    }
    private void showRemoveBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Remove Book");

        // Set up the form for removing a book
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Book> bookComboBox = new ComboBox<>();
        bookComboBox.setPromptText("Select a book to remove");
        bookComboBox.setItems(booksInTable); // Assuming booksInTable is your ObservableList<Book>

        grid.add(new Label("Select Book:"), 0, 0);
        grid.add(bookComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handling the response from the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return bookComboBox.getValue();
            }
            return null;
        });

        // Show the dialog and capture the result
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                controller.deleteBook(book);
            } catch (BooksDbException e) {
                showAlertAndWait("Error: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    private Optional<Book> showSelectBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Select a Book");
        dialog.setHeaderText("Choose a book from the list");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the ComboBox for book selection
        ComboBox<Book> bookComboBox = new ComboBox<>();
        bookComboBox.setItems(booksInTable);
        bookComboBox.setConverter(new StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                return book == null ? "" : book.getTitle() + " (" + book.getIsbn() + ")";
            }

            @Override
            public Book fromString(String string) {
                return null; // No need for this
            }
        });

        // Set the ComboBox as the content of the dialog.
        dialog.getDialogPane().setContent(bookComboBox);

        // Convert the result to a Book object when the OK button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return bookComboBox.getValue();
            }
            return null;
        });

        // Show the dialog and capture the result
        return dialog.showAndWait();
    }

    private void showUpdateBookDialog(Book bookToUpdate) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Update Book");
        dialog.setResizable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // TextFields and DatePicker for book details
        TextField titleField = new TextField(bookToUpdate.getTitle());
        TextField isbnField = new TextField(bookToUpdate.getIsbn());
        DatePicker publishedDatePicker = new DatePicker(bookToUpdate.getPublished());
        TextArea descriptionArea = new TextArea(bookToUpdate.getStoryLine());

        // Adding components to the grid
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("ISBN:"), 0, 1);
        grid.add(isbnField, 1, 1);
        grid.add(new Label("Published Date:"), 0, 2);
        grid.add(publishedDatePicker, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descriptionArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Create a new Book object to capture updates
                Book updatedBook = new Book(
                        bookToUpdate.getBookId(), // Retain the same book ID
                        isbnField.getText(),
                        titleField.getText(),
                        publishedDatePicker.getValue(),
                        bookToUpdate.getGenres(), // Use existing genres
                        bookToUpdate.getAuthors() // Use existing authors
                );

                updatedBook.setStoryLine(descriptionArea.getText());

                System.out.println("Dialog updated book details: " + updatedBook);
                return updatedBook;
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(updatedBook -> {
            System.out.println("Sending updated book to controller: " + updatedBook);
            try {
                controller.updateBook(updatedBook);
            } catch (BooksDbException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showChangeRatingDialog(Book book) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Change Book Rating");
        dialog.setHeaderText("Change rating for: " + book.getTitle());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Spinner<Integer> ratingSpinner = new Spinner<>(1, 5, book.getRating(), 1); // Adjust range as needed
        grid.add(new Label("Rating:"), 0, 0);
        grid.add(ratingSpinner, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? ratingSpinner.getValue() : null);

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(newRating -> {
            try {
                System.out.println("Updating rating for book: " + book.getTitle() + " to " + newRating);
                controller.updateBookRating(book, newRating);
            } catch (BooksDbException e) {
                showAlertAndWait("Error updating rating: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }


}

