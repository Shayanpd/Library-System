package com.example.labb2dbt.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Author class represents an author of books.
 * It contains information such as author ID, name, birthdate, and a list of books associated with the author.
 */
public class Author {
    private int authorId;
    private String name;
    private LocalDate birthDate;
    private ArrayList<Book> books;

    /**
     * Constructs an Author object with the specified author ID, name, and birthdate.
     *
     * @param authorId   The unique identifier for the author.
     * @param name       The name of the author.
     * @param birthDate  The birthdate of the author.
     */
    public Author(int authorId, String name, LocalDate birthDate) {
        this.authorId = authorId;
        this.name = name;
        this.birthDate = birthDate;
    }

    /**
     * Constructs an Author object with the specified name and birthdate.
     *
     * @param name       The name of the author.
     * @param birthDate  The birthdate of the author.
     */
    public Author(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    /**
     * Gets the name of the author.
     *
     * @return The name of the author.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the author.
     *
     * @param name The new name of the author.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the birthdate of the author.
     *
     * @return The birthdate of the author.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birthdate of the author.
     *
     * @param birthDate The new birthdate of the author.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the unique identifier for the author.
     *
     * @return The author ID.
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * Gets a copy of the list of books associated with the author.
     *
     * @return A copy of the list of books.
     */
    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Provides a string representation of the Author object.
     *
     * @return A string representation containing the author's name and birthdate.
     */
    @Override
    public String toString() {
        return  name + '\'' +
                ", birthDate=" + birthDate ;
    }
}
