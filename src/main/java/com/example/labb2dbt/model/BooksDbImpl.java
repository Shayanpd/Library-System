package com.example.labb2dbt.model;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

public class BooksDbImpl implements BooksDbInterface{
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    @Override
    public boolean connect(String dbName, String username, String password) throws BooksDbException{
        // Creating a Mongo client
        //mongodb://localhost:27017/book_database
        try {
            String uri = "mongodb+srv://" + username + ":" + password + "@labb2dbt.qej8vzl.mongodb.net/?retryWrites=true&w=majority";
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connected successfully to database: " + dbName);
            return true;
        } catch (MongoException e) {
            throw new BooksDbException("Could not connect to MongoDB database", e);
        }
    }

    @Override
    public void disconnect() throws BooksDbException {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            throw new BooksDbException("Error closing MongoDB connection", e);
        }
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
