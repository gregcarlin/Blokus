package edu.brown.cs.blokus.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


/**
  * Manages connections to the database.
  */
public class Database implements AutoCloseable {
  private final MongoClient client;
  private final MongoDatabase db;

  /**
    * Creates a new database connection.
    * @param dbName the name of the database to connect to
    */
  public Database(String dbName) {
    client = new MongoClient();
    db = client.getDatabase(dbName);
  }

  @Override
  public void close() {
    client.close();
  }
}
