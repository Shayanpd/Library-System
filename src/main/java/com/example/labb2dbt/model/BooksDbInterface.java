package com.example.labb2dbt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 *
 * NB! The methods in the implementation must catch the SQL/MongoDBExceptions thrown
 * by the underlying driver, wrap in a BooksDbException and then re-throw the latter
 * exception. This way the interface is the same for both implementations, because the
 * exception type in the method signatures is the same. More info in BooksDbException.java.
 * 
 * @author anderslm@kth.se
 */
public interface BooksDbInterface {

    /**
     * Establishes a connection to a MongoDB server and database.
     *
     * @param database The database to connect to.
     * @param username The name of the MongoDB user.
     * @param password the password of the user.
     * @return {@code true} if the connection is successfully established, {@code false} otherwise.
     * @throws BooksDbException If an error occurs while attempting to connect to the MongoDB server or database.
     */
    boolean connect(String database, String username, String password) throws BooksDbException;

    /**
     * Disconnects from the database.
     *
     * @throws BooksDbException If an error occurs while disconnecting from the database.
     */
    void disconnect() throws BooksDbException;
    /**
     * Adds a new book to the database.
     *
     * @param book The book to be added.
     * @throws BooksDbException If an error occurs while adding the book to the database.
     */
    void addBook(Book book) throws BooksDbException;
    /**
     * Inserts a new author into the database.
     * @param author The author to be added.
     * @throws BooksDbException If there is an issue with the database operation.
     */
    void addAuthor(Author author) throws BooksDbException;

    /**
     * Updates the information of an existing book.
     *
     * @param book The book object containing the updated information.
     * @throws BooksDbException If an error occurs while updating the book in the database.
     *
     */
    void updateBook(Book book) throws BooksDbException;

    /**
     * Deletes a book from the database based on its ID.
     *
     * @param bookId The ID of the book to be deleted.
     * @throws BooksDbException If an error occurs while deleting the book from the database.
     */
    void deleteBook(Book book) throws BooksDbException;

    /**
     * Updates the rating of a book in the database.
     *
     * @param book      The Book object representing the book to update.
     * @param newRating The new rating to set for the book.
     * @throws BooksDbException If an error occurs while updating the book rating in the database.
     *                          This may include SQL exceptions or other issues related to database access.
     * @see Book
     */
    void updateBookRating(Book book, int newRating) throws BooksDbException;

    /**
     * Searches for books by author name.
     *
     * @param author The author's name to search for.
     * @return A list of books matching the specified author.
     * @throws BooksDbException If an error occurs while searching in the database.
     */
    List<Book> searchBooksByAuthor(String author) throws BooksDbException;

    /**
     * Searches for books by genre.
     *
     * @param genre The genre to search for.
     * @return A list of books matching the specified genre.
     * @throws BooksDbException If an error occurs while searching in the database.
     */
    List<Book> searchBooksByGenre(String genre) throws BooksDbException;

    /**
     * Searches for books by title.
     *
     * @param title The title to search for.
     * @return A list of books matching the specified title.
     * @throws BooksDbException If an error occurs while searching in the database.
     */
    List<Book> searchBooksByTitle(String title) throws BooksDbException;

    /**
     * Searches for books by ISBN.
     *
     * @param isbn The ISBN to search for.
     * @return A list of books matching the specified ISBN.
     * @throws BooksDbException If an error occurs while searching in the database.
     */
    List<Book> searchBooksByISBN(String isbn) throws BooksDbException;

    /**
     * Searches for books by rating.
     *
     * @param rating The rating to search for.
     * @return A list of books matching the specified rating.
     * @throws BooksDbException If an error occurs while searching in the database.
     */
    List<Book> searchBooksByRating(String rating) throws BooksDbException;

    /**
     * Retrieves all authors from the database.
     * @return A list of authors.
     * @throws BooksDbException If there is an issue with the database operation.
     */
    List<Author> getAllAuthors() throws BooksDbException;

}
