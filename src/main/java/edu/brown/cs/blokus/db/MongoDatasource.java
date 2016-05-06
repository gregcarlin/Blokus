package edu.brown.cs.blokus.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
public class MongoDatasource implements Datasource {
  public static final String DEFAULT_HOST = "104.236.87.248";
  public static final int DEFAULT_PORT = 27017;
  public static final String DEFAULT_DB = "blokus";

  // maps sessionHash to userId
  public static final Map<String, String> SESSIONS = new HashMap<>();

  // the fields necessary to create a game settings object
  private static final Document SETTINGS_PROJECTION = new Document()
    .append("_id", true)
    .append("params", true)
    .append("state", true)
    .append("players", true)
    .append("curr_move", true);

  private final MongoClient client;
  private final MongoDatabase db;
  private final MongoCollection<Document> users;
  private final MongoCollection<Document> games;

  /**
    * Creates a new database connection with default values.
    */
  public MongoDatasource() {
    this(DEFAULT_DB);
  }

  /**
    * Creates a new database connection with default values.
    * @param dbName the name of the database to connect to
    */
  public MongoDatasource(String dbName) {
    this(DEFAULT_HOST, dbName);
  }

  /**
    * Creates a new database connection with default values.
    * @param host the address of the database host
    * @param dbName the name of the database to connect to
    */
  public MongoDatasource(String host, String dbName) {
    this(host, DEFAULT_PORT, dbName);
  }

  /**
    * Creates a new database connection.
    * @param host the address of the database host
    * @param port the port where the database is running
    * @param dbName the name of the database to connect to
    */
  public MongoDatasource(String host, int port, String dbName) {
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

  @Override
  public String createUser(String user, String pass) {
    Document userDoc = getUser(user);
    if (userDoc != null) { return null; }

    Document newUserDoc = new Document();
    newUserDoc.append("username", user);
    newUserDoc.append("password", BCrypt.hashpw(pass, BCrypt.gensalt()));
    users.insertOne(newUserDoc);

    return getUser(user).getObjectId("_id").toString();
  }

  @Override
  public String getUserId(String user, String pass) {
    Document userDoc = getUser(user);
    if (userDoc == null) { return null; }

    String actualPass = userDoc.getString("password");
    if (!BCrypt.checkpw(pass, actualPass)) { return null; }

    return userDoc.getObjectId("_id").toString();
  }

  @Override
  public String logIn(String userId) {
    String hash = UUID.randomUUID().toString();
    SESSIONS.put(hash, userId);
    return hash;
  }

  @Override
  public String getUserId(String hash) {
    return SESSIONS.get(hash);
  }

  @Override
  public String getName(String id) {
    Document doc = users.find(new Document("_id", new ObjectId(id))).first();
    return doc == null ? null : doc.getString("username");
  }

  /**
    * Creates a game settings object based on values in the given document.
    * @param doc a document from the games collection
    * @return a GameSettings object
    */
  private static GameSettings parseSettings(Document doc) {
    GameSettings.Builder builder
      = new GameSettings.Builder(doc.getObjectId("_id").toString());

    @SuppressWarnings("unchecked")
    List<Document> playerDocs = doc.get("players", List.class);
    final int playerCount = playerDocs.size();
    for (int i = 0; i < playerCount; i++) {
      Document playerDoc = playerDocs.get(i);
      if (playerDoc == null) { continue; }

      String playerId = playerDoc.getObjectId("_id").toString();

      List<Shape> shapes = new ArrayList<>();
      @SuppressWarnings("unchecked")
      List<Integer> pieces = playerDoc.get("pieces", List.class);
      for (int piece : pieces) {
        shapes.add(Shape.values()[piece]);
      }

      Turn turn = Turn.values()[i];
      builder.player(turn, new Player(playerId, shapes,
            playerDoc.getInteger("score"), playerDoc.getBoolean("playing")));
    }

    Document params = doc.get("params", Document.class);
    Document lastMoveDoc = doc.get("curr_move", Document.class);
    return builder
      .type(GameSettings.Type.values()[params.getInteger("privacy")])
      .state(GameSettings.State.values()[doc.getInteger("state")])
      .maxPlayers(params.getInteger("num-players"))
      .timer(params.getInteger("timer"))
      .lastTurnTime(lastMoveDoc.getLong("timestamp"))
      .build();
  }

  @Override
  public String getGameRaw(String id) {
    FindIterable<Document> docs
      = games.find(new Document("_id", new ObjectId(id)));
    Document doc = docs.first();
    return doc == null ? null : doc.toJson();
  }

  @Override
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

    Document lastMoveDoc = gameDoc.get("curr_move", Document.class);
    int turnInt = lastMoveDoc.getInteger("turn");
    gameBuilder.setTurn(Turn.values()[turnInt < 0 ? 0 : turnInt]);

    return gameBuilder.build();
  }

  @Override
  public String saveGame(Game game) {
    GameSettings settings = game.getSettings();

    ObjectId id
      = settings.hasId() ? new ObjectId(settings.getId()) : new ObjectId();

    Document[] players = new Document[Turn.values().length];
    for (Turn turn : Turn.values()) {
      Player player = game.getPlayer(turn);
      if (player == null) { continue; }

      List<Integer> pieces = new ArrayList<>();
      for (Shape shape : player.getRemainingPieces()) {
        pieces.add(shape.ordinal());
      }

      players[turn.ordinal()] = new Document()
          .append("_id", new ObjectId(player.getId()))
          .append("pieces", pieces)
          .append("score", player.getScore())
          .append("playing", player.isPlaying());
    }

    Document gameDoc = new Document()
      .append("_id", id)
      .append("players", Arrays.asList(players))
      .append("params", new Document()
          .append("privacy", settings.getType().ordinal())
          .append("num-players", settings.getMaxPlayers())
          .append("timer", settings.getTimer()))
      .append("curr_move", new Document()
          .append("turn", game.isGameOver() ? -1 : game.getTurn().ordinal())
          .append("timestamp", settings.getLastTurnTime()))
      .append("state", settings.getState().ordinal())
      .append("board", game.getGridAsList());

    games.replaceOne(new Document("_id", id), gameDoc,
        new UpdateOptions().upsert(true));
    return id.toString();
  }

  @Override
  public List<GameSettings> getOpenGames(String without) {
    FindIterable<Document> docs = games.find(and(
        eq("params.privacy", GameSettings.Type.PUBLIC.ordinal()),
        eq("state", GameSettings.State.UNSTARTED.ordinal()),
        not(elemMatch("players", new Document("_id", new ObjectId(without))))))
      .sort(new Document("_id", -1))
      .projection(SETTINGS_PROJECTION);

    List<GameSettings> rt = new ArrayList<>();
    for (Document doc : docs) {
      rt.add(parseSettings(doc));
    }
    return rt;
  }

  @Override
  public List<GameSettings> getGamesWith(String playerId) {
    FindIterable<Document> docs = games.find(and(
          elemMatch("players", new Document("_id", new ObjectId(playerId))),
          ne("state", GameSettings.State.FINISHED.ordinal())))
      .sort(new Document("_id", -1))
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
