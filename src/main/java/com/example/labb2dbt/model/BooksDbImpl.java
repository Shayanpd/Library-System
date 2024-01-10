package com.example.labb2dbt.model;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

        List<Document> genreList = new ArrayList<>();

        for (Genre g : book.getGenres()){
            Document genreDoc = new Document();
            genreDoc.put("name", g.getGenreName());
            genreList.add(genreDoc);
        }

        List<Author> authorList = new ArrayList<>();

        for (Author a : book.getAuthors()){
            Document authorDoc = new Document();
            authorDoc.put("name", a.getName());
            authorDoc.put("dateOfBirth", a.getBirthDate());
            authorList.add(a);
        }

        doc.put("genres", genreList);
        //doc.put("authors", book.getAuthors());

        this.booksCollection.insertOne(doc);

        System.out.println("insert success");
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
    public List<Book> searchBooksByAuthor(String author) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByGenre(String genre) throws BooksDbException {
        return null;
    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }

        List<Book> result = new ArrayList<>();

        try {
            // Using regex to perform a case-insensitive search for the title
            FindIterable<Document> foundBooks = booksCollection.find(Filters.regex("title", searchTitle, "i"));

            getBooksFromDb(result, foundBooks);
        } catch (MongoException e) {
            throw new BooksDbException("Error searching books by title", e);
        }

        return result;
    }



    @Override
    public List<Book> searchBooksByISBN(String isbnString) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
            List<Book> result = new ArrayList<>();

            try {
                // Using regex to perform a case-insensitive search for the title
                FindIterable<Document> foundBooks = booksCollection.find(Filters.regex("isbn", isbnString, "i"));

                getBooksFromDb(result, foundBooks);
            } catch (MongoException e) {
                throw new BooksDbException("Error searching books by ISBN", e);
            }

            return result;

    }

    private void getBooksFromDb(List<Book> result, FindIterable<Document> foundBooks) {
        for (Document doc : foundBooks) {
            String title = doc.getString("title");
            String isbn = doc.getString("isbn");
            Date dateOfRelease = doc.getDate("dateOfRelease");
            String description = doc.getString("description");
            int rating = doc.getInteger("rating", 0);

            // Convert Date to LocalDate
            LocalDate localDateOfRelease = dateOfRelease.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Book book = new Book(isbn, title, localDateOfRelease);
            book.setStoryLine(description);
            book.setRating(rating);

            // Process genres if they are present in the document
            List<Document> genreDocs = (List<Document>) doc.get("genres");
            if (genreDocs != null) {
                ArrayList<Genre> genres = new ArrayList<>();
                for (Document genreDoc : genreDocs) {
                    genres.add(new Genre(genreDoc.getString("name")));
                }
                book.setGenres(genres);
            }

            result.add(book);
        }
    }

    @Override
    public List<Book> searchBooksByRating(String ratingString) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
        List<Book> result = new ArrayList<>();

        try {
            // Convert the rating string to an integer
            int rating = Integer.parseInt(ratingString);

            // Find books with the specified rating
            FindIterable<Document> foundBooks = booksCollection.find(Filters.eq("rating", rating));

            getBooksFromDb(result, foundBooks);
        } catch (NumberFormatException e) {
            throw new BooksDbException("Invalid rating format: " + ratingString, e);
        } catch (MongoException e) {
            throw new BooksDbException("Error searching books by rating", e);
        }

        return result;
    }

    @Override
    public void deleteAuthor(int authorId) throws BooksDbException {

    }

    @Override
    public List<Author> getAllAuthors() throws BooksDbException { //only gets the first author atm
        Document doc = this.authorsCollection.find().first();
        if (doc == null) System.out.println("doc is null");
        String name = doc.getString("name");
        Date dateOfBirth = doc.getDate("dateOfBirth");
        LocalDate convertedDate = convertToLocalDateViaInstant(dateOfBirth);

        Author a = new Author(name, convertedDate);
        List<Author> result = new ArrayList<>();
        result.add(a);
        return result;
    }

    @Override
    public List<Genre> getAllGenres() throws BooksDbException {
        return null;
    }
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) { //convert Date to LocalDate, maybe need later
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
