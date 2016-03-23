package edu.brown.cs.blokus.db;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;


/**
  * Manages connections to the database.
  */
public class Database implements AutoCloseable {
  private static final String USER_COLLECTION = "users";
  private static final String GAME_COLLECTION = "games";

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

  private Document getUser(String username) {
    FindIterable<Document> docs = db.getCollection(USER_COLLECTION)
      .find(new Document("username", username));
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
    db.getCollection(USER_COLLECTION).insertOne(newUserDoc);

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

  @Override
  public void close() {
    client.close();
  }
}
