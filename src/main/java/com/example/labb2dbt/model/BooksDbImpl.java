package com.example.labb2dbt.model;

import java.util.ArrayList;
import java.util.List;

public class BooksDbImpl implements BooksDbInterface{
    @Override
    public boolean connect(String database, String username, String password) throws BooksDbException {
        return false;
    }

    @Override
    public void disconnect() throws BooksDbException {

    }

    @Override
    public void addBook(Book book) throws BooksDbException {

    }

    @Override
    public void addAuthor(Author author) throws BooksDbException {

    }

    @Override
    public void updateBook(Book book) throws BooksDbException {

    }

    @Override
    public void deleteBook(int bookId) throws BooksDbException {

    }

    @Override
    public void updateBookRating(Book book, int newRating) throws BooksDbException {

    }

    @Override
    public ArrayList<Book> getBooksFromDB() throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByISBN(String isbn) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByRating(String rating) throws BooksDbException {
        return null;
    }

    @Override
    public void deleteAuthor(int authorId) throws BooksDbException {

    }

    @Override
    public List<Author> getAllAuthors() throws BooksDbException {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() throws BooksDbException {
        return null;
    }
}
