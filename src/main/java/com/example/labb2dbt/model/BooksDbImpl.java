package com.example.labb2dbt.model;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BooksDbImpl implements BooksDbInterface{
    public MongoClient mongoClient;
    public MongoDatabase mongoDatabase;

    private MongoCollection<Document> booksCollection;
    private MongoCollection<Document> authorsCollection;
    private MongoCollection<Document> genresCollection;


    @Override
    public boolean connect(String dbName, String username, String password) throws BooksDbException{

        try {
            String uri = "mongodb+srv://" + username + ":" + password + "@labb2dbt.qej8vzl.mongodb.net/?retryWrites=true&w=majority";
            mongoClient = MongoClients.create(uri);
            mongoDatabase = mongoClient.getDatabase(dbName);

            this.booksCollection = this.mongoDatabase.getCollection("books");
            this.authorsCollection = this.mongoDatabase.getCollection("authors");
            this.genresCollection = this.mongoDatabase.getCollection("genres");

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
        Document doc = new Document();
        doc.put("title", book.getTitle());
        doc.put("isbn", book.getIsbn());
        doc.put("dateOfRelease", book.getPublished());
        doc.put("description", book.getStoryLine());
        doc.put("rating", book.getRating());
        //doc.put("genres", book.getGenres());
        //doc.put("authors", book.getAuthors());

        this.booksCollection.insertOne(doc);

        System.out.println("insert success?");
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
    public List<Book> searchBooksByTitle(String searchTitle) throws BooksDbException { //work in progress
        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();
        Document doc;
        this.booksCollection.find(Filters.eq("title", searchTitle));


        System.out.println("res " + result.toString());





        return result;
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
