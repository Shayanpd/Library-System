package com.example.labb2dbt;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoDBTest {

    public static void main(String[] args) {
        // Connect to MongoDB instance running on localhost
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Select the database and collection
            MongoDatabase database = mongoClient.getDatabase("testDB");
            MongoCollection<Document> collection = database.getCollection("testCollection");

            // Create a document to insert
            Document doc = new Document("name", "John Doe")
                    .append("age", 30)
                    .append("city", "New York");

            // Insert the document into the collection
            collection.insertOne(doc);
            System.out.println("Document inserted successfully");

            // Retrieve the document
            Document myDoc = collection.find().first();
            System.out.println("Retrieved document: " + myDoc.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

