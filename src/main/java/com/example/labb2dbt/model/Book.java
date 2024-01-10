package com.example.labb2dbt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book {

    private int bookId;
    private String isbn;
    private String title;
    private LocalDate published;
    private String storyLine;
    private int rating;

    private ArrayList<Genre> genres;
    private ArrayList<Author> authors;

    /**
     * Constructs a Book object with the specified book ID, ISBN, title, and published date.
     *
     * @param bookId    The unique identifier for the book.
     * @param isbn      The ISBN of the book.
     * @param title     The title of the book.
     * @param published The published date of the book.
     */
    public Book(int bookId, String isbn, String title, LocalDate published) {
        this(bookId, isbn, title, published, new ArrayList<Genre>(), new ArrayList<Author>());
    }
    /**
     * Constructs a Book object with the specified book ID, ISBN, title, published date,
     * genres, and authors.
     *
     * @param bookId    The unique identifier for the book.
     * @param isbn      The ISBN of the book.
     * @param title     The title of the book.
     * @param published The published date of the book.
     * @param genres    The list of genres associated with the book.
     * @param authors   The list of authors associated with the book.
     */
    public Book(int bookId, String isbn, String title, LocalDate published, ArrayList<Genre> genres, ArrayList<Author> authors) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        this.authors = authors;
        this.genres = genres;

    }
    /**
     * Sets the rating of the book.
     *
     * @param rating The rating to set.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Constructs a Book object with the specified ISBN, title, and published date.
     *
     * @param isbn      The ISBN of the book.
     * @param title     The title of the book.
     * @param published The published date of the book.
     */

    public Book(String isbn, String title, LocalDate published) {
        this(0, isbn, title, published);
    }


    /**
     * Sets the book ID for the book.
     *
     * @param bookId The book's ID to set.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Sets the genres associated with the book.
     *
     * @param genres The list of genres to set.
     */
    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Sets the authors associated with the book.
     *
     * @param authors The list of authors to set.
     */
    public void setAuthors(List<Author> authors) {
        this.authors = new ArrayList<>(authors);
    }

    /**
     * Gets the unique identifier for the book.
     *
     * @return The book ID.
     */
    public int getBookId() {
        return bookId;
    }
    /**
     * Gets the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }
    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Gets the published date of the book.
     *
     * @return The published date of the book.
     */
    public LocalDate getPublished() {
        return published;
    }
    /**
     * Gets the story line of the book.
     *
     * @return The story line of the book.
     */
    public String getStoryLine() {
        return this.storyLine;
    }

    /**
     * Gets a copy of the list of authors associated with the book.
     *
     * @return A copy of the list of authors.
     */
    public ArrayList<Author> getAuthors() {
        return new ArrayList<>(authors);
    }

    /**
     * Adds an author to the list of authors associated with the book.
     *
     * @param author The author to add.
     */
    public void addAuthor(Author author) {
        if (!authors.contains(author)) authors.add(author);
    }

    /**
     * Removes an author from the list of authors associated with the book.
     *
     * @param author The author to remove.
     */
    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    /**
     * Adds a genre to the list of genres associated with the book.
     *
     * @param genre The genre to add.
     */
    public void addGenre(Genre genre) {
        if (!genres.contains(genre)) genres.add(genre);
    }

    /**
     * Removes a genre from the list of genres associated with the book.
     *
     * @param genre The genre to remove.
     */
    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    /**
     * Gets a copy of the list of genres associated with the book.
     *
     * @return A copy of the list of genres.
     */
    public ArrayList<Genre> getGenres() {
        return new ArrayList<>(genres);
    }

    /**
     * Sets the story line of the book.
     *
     * @param storyLine The story line to set.
     */
    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }
    /**
     * Sets the published date of the book.
     *
     * @param published The published date to set.
     */
    public void setPublished(LocalDate published) {
        this.published = published;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn The ISBN to set.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gets the rating of the book.
     *
     * @return The book's rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets the genres associated with the book as a string.
     *
     * @return A string representation of the genres, separated by commas.
     */
    public String getGenresAsString() {
        return genres.stream()
                .map(Genre::getGenreName) // Assuming Genre has a getGenreName() method
                .collect(Collectors.joining(", "));
    }
    /**
     * Provides a string representation of the Book object.
     *
     * @return A string representation containing book ID, ISBN, title, published date, story line, genres, and authors.
     */
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", published=" + published +
                ", storyLine='" + storyLine + '\'' +
                ", genres=" + genres +
                ", authors=" + authors +
                '}';
    }


}
