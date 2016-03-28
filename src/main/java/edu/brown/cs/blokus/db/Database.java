package edu.brown.cs.blokus.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.*;


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

  /**
    * Creates a game settings object based on values in the given document.
    * @param doc a document from the games collection
    * @return a GameSettings object
    */
  private static GameSettings parseSettings(Document doc) {
    // TODO read in actual data besides _id
    return new GameSettings.Builder(doc.getString("_id"))
      .build();
  }

  /**
    * Gets a list of publicly listed games that aren't full.
    * @return a list of game settings
    */
  public List<GameSettings> getJoinableGames() {
    FindIterable<Document> it = games.find(
        and(
          eq("params.privacy", GameSettings.Type.PUBLIC.ordinal()),
          lt("players.length", "params.num-players")
        ));

    List<GameSettings> rt = new ArrayList<>();
    for (Document doc : it) {
      rt.add(parseSettings(doc));
    }
    return rt;
  }

  /**
    * Gets a game with a given id.
    * @param id the id of the game to find
    * @return the game, or null if not found
    */
  public Game getGame(String id) {
    FindIterable<Document> docs = games.find(new Document("_id", id));
    Document gameDoc = docs.first();
    if (gameDoc == null) { return null; }

    // TODO set values besides settings
    return new Game.Builder()
      .setSettings(parseSettings(gameDoc))
      .build();
  }

  /**
    * Saves a game to the database.
    * If a game with a matching id is already present, it is replaced.
    * @param game the game to save
    */
  public void saveGame(Game game) {
    GameSettings settings = game.getSettings();

    // TODO players, last move and state
    Document gameDoc = new Document()
      .append("_id", settings.getId())
      .append("params", new Document()
          .append("privacy", settings.getType().ordinal())
          .append("num-players", settings.getMaxPlayers())
          .append("timer", settings.getTimer()))
      .append("board", game.getGrid());
  }

  @Override
  public void close() {
    client.close();
  }
}
