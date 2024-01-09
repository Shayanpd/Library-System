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
    public List<Book> searchBooksByTitle(String searchTitle) throws BooksDbException {
        List<Book> result = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();

        Document query = new Document("title", new Document("$regex", searchTitle).append("$options", "i"));

        try (MongoCursor<Document> cursor = booksCollection.find(query).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int bookID = doc.getInteger("bookID");
                String isbn = doc.getString("isbn");
                String title = doc.getString("title");
                LocalDate publishedDate = doc.getDate("dateOfRelease").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String storyLine = doc.getString("description");
                int rating = doc.getInteger("rating");

                // Handling genres array
                List<Genre> genres = new ArrayList<>();
                List<Document> genresDocs = (List<Document>) doc.get("genres");
                for (Document genreDoc : genresDocs) {
                    String genreName = genreDoc.getString("name");
                    genres.add(new Genre(genreName));
                }

                // Handling authors array (assuming an Author class with a name field)
                List<Author> authors = new ArrayList<>();
                List<Document> authorsDocs = (List<Document>) doc.get("authors");
                for (Document authorDoc : authorsDocs) {
                    String authorName = authorDoc.getString("name");
                    authors.add(new Author(authorName, LocalDate.now()));
                }

                Book book = new Book(bookID, isbn, title, publishedDate);
                book.setStoryLine(storyLine);
                book.setRating(rating);
                book.setGenres((ArrayList<Genre>) genres);
                book.setAuthors((ArrayList<Author>) authors);

                result.add(book);
            }
        } catch (Exception e) {
            throw new BooksDbException("Error searching books by title", e);
        }
        System.out.println(result.toString()); //debugging
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
