package edu.brown.cs.blokus.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.Turn;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.*;


/**
  * Manages connections to the database.
  */
public class Database implements AutoCloseable {
  public static final String DEFAULT_HOST = "104.236.87.248";
  public static final int DEFAULT_PORT = 27017;
  public static final String DEFAULT_DB = "blokus";

  // maps sessionHash to userId
  public static final Map<String, String> sessions = new HashMap<>();

  // the number of games to list in a single page
  private static final int PAGE_SIZE = 20;
  // the fields necessary to create a game settings object
  private static final Document SETTINGS_PROJECTION = new Document()
    .append("_id", true)
    .append("params", true)
    .append("state", true);

  private final MongoClient client;
  private final MongoDatabase db;
  private final MongoCollection<Document> users;
  private final MongoCollection<Document> games;

  /**
    * Creates a new database connection with default values.
    */
  public Database() {
    this(DEFAULT_DB);
  }

  /**
    * Creates a new database connection with default values.
    * @param dbName the name of the database to connect to
    */
  public Database(String dbName) {
    this(DEFAULT_HOST, dbName);
  }

  /**
    * Creates a new database connection with default values.
    * @param host the address of the database host
    * @param dbName the name of the database to connect to
    */
  public Database(String host, String dbName) {
    this(host, DEFAULT_PORT, dbName);
  }

  /**
    * Creates a new database connection.
    * @param host the address of the database host
    * @param port the port where the database is running
    * @param dbName the name of the database to connect to
    */
  public Database(String host, int port, String dbName) {
    client = new MongoClient(host, port);
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
    newUserDoc.append("password", BCrypt.hashpw(pass, BCrypt.gensalt()));
    users.insertOne(newUserDoc);

    return getUser(user).getObjectId("_id").toString();
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

    return userDoc.getObjectId("_id").toString();
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
    Document params = doc.get("params", Document.class);
    return new GameSettings.Builder(doc.getObjectId("_id").toString())
      .type(GameSettings.Type.values()[params.getInteger("privacy")])
      .state(GameSettings.State.values()[doc.getInteger("state")])
      .maxPlayers(params.getInteger("num-players"))
      .timer(params.getInteger("timer"))
      .build();
  }

  /**
    * Gets a game with a given id.
    * @param id the id of the game to find
    * @return the game, or null if not found
    */
  public Game getGame(String id) {
    FindIterable<Document> docs
      = games.find(new Document("_id", new ObjectId(id)));
    Document gameDoc = docs.first();
    if (gameDoc == null) { return null; }

    Game.Builder gameBuilder = new Game.Builder();
    gameBuilder.setSettings(parseSettings(gameDoc));

    @SuppressWarnings("unchecked")
    List<List<Integer>> board = (List<List<Integer>>) gameDoc.get("board");
    int[][] grid = new int[board.size()][];
    for (int i = 0; i < grid.length; i++) {
      List<Integer> row = board.get(i);
      grid[i] = new int[row.size()];
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = row.get(j);
      }
    }
    gameBuilder.setGrid(grid);

    @SuppressWarnings("unchecked")
    List<Document> playerDocs = gameDoc.get("players", List.class);
    final int playerCount = playerDocs.size();
    for (int i = 0; i < playerCount; i++) {
      Document playerDoc = playerDocs.get(i);
      String playerId = playerDoc.getObjectId("_id").toString();

      List<Shape> shapes = new ArrayList<>();
      @SuppressWarnings("unchecked")
      List<Integer> pieces = playerDoc.get("pieces", List.class);
      for (int piece : pieces) {
        shapes.add(Shape.values()[piece]);
      }

      Turn turn = Turn.values()[i];
      gameBuilder.setPlayer(turn, new Player(playerId, shapes,
            playerDoc.getInteger("score"), playerDoc.getBoolean("playing")));
    }

    Document lastMoveDoc = gameDoc.get("curr_move", Document.class);
    gameBuilder.setTurn(Turn.values()[lastMoveDoc.getInteger("turn")]);
    gameBuilder.setLastTurnTime(lastMoveDoc.getLong("timestamp"));

    return gameBuilder.build();
  }

  /**
    * Saves a game to the database.
    * If a game with a matching id is already present, it is replaced.
    * @param game the game to save
    * @return the id of the saved game
    */
  public String saveGame(Game game) {
    GameSettings settings = game.getSettings();

    ObjectId id
      = settings.hasId() ? new ObjectId(settings.getId()) : new ObjectId();

    List<Document> players = new ArrayList<>();
    for (Turn turn : Turn.values()) {
      Player player = game.getPlayer(turn);

      List<Integer> pieces = new ArrayList<>();
      for (Shape shape : player.getRemainingPieces()) {
        pieces.add(shape.ordinal());
      }

      players.add(new Document()
          .append("_id", new ObjectId(player.getId()))
          .append("pieces", pieces)
          .append("score", player.getScore())
          .append("playing", player.isPlaying()));
    }

    Document gameDoc = new Document()
      .append("_id", id)
      .append("players", players)
      .append("params", new Document()
          .append("privacy", settings.getType().ordinal())
          .append("num-players", settings.getMaxPlayers())
          .append("timer", settings.getTimer()))
      .append("curr_move", new Document()
          .append("turn", game.getTurn().ordinal())
          .append("timestamp", game.getLastTurnTime()))
      .append("state", settings.getState().ordinal())
      .append("board", game.getGridAsList());

    games.replaceOne(new Document("_id", id), gameDoc,
        new UpdateOptions().upsert(true));
    return id.toString();
  }

  /**
    * Gets a list of games that are public and haven't been started yet.
    * @param page the 0-indexed page of data to load
    * @return a set of game settings representing each game
    */
  public List<GameSettings> getOpenGames(int page) {
    FindIterable<Document> docs = games.find(new Document()
        .append("params.privacy", GameSettings.Type.PUBLIC.ordinal())
        .append("state", GameSettings.State.UNSTARTED.ordinal()))
      .skip(page * PAGE_SIZE)
      .projection(SETTINGS_PROJECTION);

    List<GameSettings> rt = new ArrayList<>();
    for (Document doc : docs) {
      rt.add(parseSettings(doc));
    }
    return rt;
  }

  /**
    * Gets all games that a given player has joined.
    * Excludes games that are finished.
    * @param playerId the unique id of the player
    * @return a set of game settings representing each game
    */
  public List<GameSettings> getGamesWith(String playerId) {
    FindIterable<Document> docs = games.find(and(
          elemMatch("players", new Document("_id", new ObjectId(playerId))),
          ne("state", GameSettings.State.FINISHED.ordinal())))
      .projection(SETTINGS_PROJECTION);

    List<GameSettings> rt = new ArrayList<>();
    for (Document doc : docs) {
      rt.add(parseSettings(doc));
    }
    return rt;
  }

  /**
    * Wipes all data in this database.
    */
  void empty() {
    db.drop();
  }

  @Override
  public void close() {
    client.close();
  }
}
