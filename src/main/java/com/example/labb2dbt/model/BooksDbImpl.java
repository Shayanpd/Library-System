package com.example.labb2dbt.model;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
    public void addBook(Book book) throws BooksDbException {//TODO: fix authors(does not work currently)
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

        List<Document> authorList = new ArrayList<>();

        for (Author a : book.getAuthors()){
            Document authorDoc = new Document();
            authorDoc.put("name", a.getName());
            authorDoc.put("dateOfBirth", a.getBirthDate());
            authorList.add(authorDoc);
        }

        doc.put("genres", genreList);
        doc.put("authors", authorList);

        this.booksCollection.insertOne(doc);

        System.out.println("insert success");
    }

    @Override
    public void addAuthor(Author author) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }

        try {
            Document authorDoc = new Document()
                    .append("name", author.getName())
                    .append("dateOfBirth", author.getBirthDate());

            authorsCollection.insertOne(authorDoc);
        } catch (MongoException e) {
            throw new BooksDbException("Error adding author to MongoDB database", e);
        }
    }


    @Override
    public void updateBook(Book book) throws BooksDbException {//TODO

    }

    @Override
    public void deleteBook(Book book) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
        String isbn = book.getIsbn();
        try {
            // Create a filter to find the book by ISBN
            Bson filter = Filters.eq("isbn", isbn);

            // Perform the deletion
            DeleteResult result = booksCollection.deleteOne(filter);

            if (result.getDeletedCount() == 0) {
                throw new BooksDbException("No book found with ISBN: " + isbn);
            }
        } catch (MongoException e) {
            throw new BooksDbException("Error deleting book from MongoDB database", e);
        }
    }


    @Override
    public void updateBookRating(Book book, int newRating) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
        String isbn = book.getIsbn();
        try {
            // Create a filter to search for the book by ISBN
            Bson filter = Filters.eq("isbn", isbn);

            // Create an update operation to set the new rating
            Bson updateOperation = Updates.set("rating", newRating);

            // Perform the update
            UpdateResult result = booksCollection.updateOne(filter, updateOperation);

            if (result.getMatchedCount() == 0) {
                throw new BooksDbException("No book found with ISBN: " + isbn);
            }
            if (result.getModifiedCount() == 0) {
                throw new BooksDbException("Book rating not updated for ISBN: " + isbn);
            }
        } catch (MongoException e) {
            throw new BooksDbException("Error updating book rating in MongoDB database", e);
        }
    }


    @Override
    public List<Book> searchBooksByAuthor(String author) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
        List<Book> result = new ArrayList<>();

        try {
            // Using regex to perform a case-insensitive search for the author
            Pattern pattern = Pattern.compile(author, Pattern.CASE_INSENSITIVE);
            Bson filter = Filters.elemMatch("authors", Filters.regex("name", pattern));

            FindIterable<Document> foundBooks = booksCollection.find(filter);
            getBooksFromDb(result, foundBooks);
        } catch (MongoException e) {
            throw new BooksDbException("Error searching books by Author", e);
        }

        return result;
    }


    @Override
    public List<Book> searchBooksByGenre(String genre) throws BooksDbException {
        if (mongoDatabase == null) {
            throw new BooksDbException("Not connected to the database");
        }
        List<Book> result = new ArrayList<>();

        try {
            // Using regex to perform a case-insensitive partial search for the genre
            Pattern pattern = Pattern.compile(genre, Pattern.CASE_INSENSITIVE);
            Bson filter = Filters.elemMatch("genres", Filters.regex("name", pattern));

            FindIterable<Document> foundBooks = booksCollection.find(filter);
            getBooksFromDb(result, foundBooks);
        } catch (MongoException e) {
            throw new BooksDbException("Error searching books by genre", e);
        }

        return result;
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
            String bookId = doc.getString("bookId");
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
    public List<Author> getAllAuthors() throws BooksDbException {
        List<Author> authors = new ArrayList<>();
        try {
            // Query all documents in the authors collection
            FindIterable<Document> documents = authorsCollection.find();

            // Iterate over each document and convert it to an Author object
            for (Document doc : documents) {
                String name = doc.getString("name");
                Date dateOfBirth = doc.getDate("dateOfBirth");

                // Convert Date to LocalDate
                LocalDate localDateOfBirth = convertToLocalDateViaInstant(dateOfBirth);

                // Create a new Author object and add it to the list
                Author author = new Author(name, localDateOfBirth);
                authors.add(author);
            }
        } catch (MongoException e) {
            throw new BooksDbException("Error retrieving authors from MongoDB database", e);
        }

        return authors;
    }


    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) { //convert Date to LocalDate, maybe need later
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
