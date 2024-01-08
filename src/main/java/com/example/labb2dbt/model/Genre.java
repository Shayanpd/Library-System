package com.example.labb2dbt.model;

import java.util.ArrayList;

/**
 * Represents a genre in the book database.
 * A genre can be associated with multiple books.
 */
public class Genre {

    private int genreId;
    private final String name;
    private ArrayList<Book> books;

    /**
     * Constructs a Genre with the specified genre ID and name.
     *
     * @param genreId The genre ID.
     * @param name The name of the genre.
     */
    public Genre(int genreId, String name){
        this.genreId = genreId;
        this.name = name;
        this.books = new ArrayList<Book>();
    }

    /**
     * Constructs a Genre with the specified name.
     *
     * @param name The name of the genre.
     */
    public Genre(String name){
        this.name = name;
        this.books = new ArrayList<Book>();
    }

    /**
     * Adds a book to the genre's list of associated books.
     *
     * @param book The book to be added.
     */
    public void addBook(Book book){
        if(books.contains(book)) books.add(book);
    }

    /**
     * Removes a book from the genre's list of associated books.
     *
     * @param book The book to be removed.
     */
    public void removeBook(Book book)
    {
        books.remove(book);
    }

    /**
     * Gets a copy of the list of books associated with this genre.
     *
     * @return An ArrayList of books associated with this genre.
     */
    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Gets the name of the genre.
     *
     * @return The name of the genre.
     */
    public String getGenreName()
    {
        return name;
    }

    /**
     * Gets the genre ID.
     *
     * @return The genre ID.
     */
    public int getGenreId()
    {
        return genreId;
    }

    /**
     * Returns a string representation of the genre.
     *
     * @return A string containing the genre's ID and name.
     */
    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", name='" + name + '\'' +
                '}';
    }
}


