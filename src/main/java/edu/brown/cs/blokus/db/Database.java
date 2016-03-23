package edu.brown.cs.blokus.db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;


/**
  * Manages connections to the database.
  */
public class Database implements AutoCloseable {
  private final MongoClient client;
  private final MongoDatabase db;
  private final MongoCollection<Document> users;
  private final MongoCollection<Document> games;
  // maps userId to sessionHash
  private final Map<String, String> sessions = new HashMap<>();

  /**
    * Creates a new database connection.
    * @param dbName the name of the database to connect to
    */
  public Database(String dbName) {
    client = new MongoClient();
    db = client.getDatabase(dbName);
    users = db.getCollection("users");
    games = db.getCollection("games");
  }

  private Document getUser(String username) {
    FindIterable<Document> docs
      = users.find(new Document("username", username));
    return docs.first();
  }

  /**
    * Creates a new user with a given username and password.
    * @param user the username
    * @param pass the password
    * @return the id of the new user, of null if this username already exists
    */
  public String createUser(String user, String pass) {
    Document userDoc = getUser(user);
    if (userDoc != null) { return null; }

    Document newUserDoc = new Document();
    newUserDoc.append("username", user);
    newUserDoc.append("password", pass);
    users.insertOne(newUserDoc);

    return getUser(user).getString("_id");
  }

  /**
    * Gets the id of a user given valid credentials.
    * @param user the username
    * @param pass the password
    * @return the id of the user if credentials are valid, null otherwise
    */
  public String getUserId(String user, String pass) {
    Document userDoc = getUser(user);
    if (userDoc == null) { return null; }

    String actualPass = userDoc.getString("password");
    if (!BCrypt.checkpw(pass, actualPass)) { return null; }

    return userDoc.getString("_id");
  }

  /**
    * Creates a new session for a given user.
    * @param userId the id of the user logging in
    * @return the session hash
    */
  public String logIn(String userId) {
    String hash = UUID.randomUUID().toString();
    sessions.put(hash, userId);
    return hash;
  }

  /**
    * Gets the user id associated with a session hash.
    * @param hash the session hash provided by the client
    * @return the id of the associated user, or null if none
    */
  public String getUserId(String hash) {
    return sessions.get(hash);
  }

  @Override
  public void close() {
    client.close();
  }
}
