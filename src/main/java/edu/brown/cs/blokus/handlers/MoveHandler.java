package edu.brown.cs.blokus.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Orientation;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.db.Database;

import spark.Request;
import spark.Response;
import spark.Route;


/**
  * Handles the ajax request a client sends when it makes a move.
  */
public class MoveHandler implements Route {
  private static final Gson GSON = new Gson();
  private static final String FAIL = status(false);

  private final Database db;

  /**
    * Creates a new handler that handles move requests.
    * @param db a reference to the database
    */
  public MoveHandler(Database db) {
    this.db = db;
  }

  @Override
  public Object handle(Request req, Response res) {
  	final String user = req.attribute("user-id");
    final Game game = db.getGame(req.params("id"));

    // if it's not this user's turn
    if (!user.equals(game.getPlayer(game.getTurn()).getId())) {
      return FAIL;
    }

    JsonObject jObj = GSON.fromJson(req.body(), JsonObject.class);
    Shape shape = Shape.values()[jObj.get("piece").getAsInt()];
    Orientation orientation
      = Orientation.values()[jObj.get("orientation").getAsInt()];
    int x = jObj.get("x").getAsInt();
    int y = jObj.get("y").getAsInt();
    Move move = new Move(shape, orientation, x, y);

    // if it's an illegal move
    if (!game.isLegal(move)) {
      return FAIL;
    }

    game.makeMove(move);
    return status(true, game.getTurn().ordinal());
  }

  private static String status(boolean flag) {
    JsonObject jObj = new JsonObject();
    jObj.addProperty("success", flag);
    return GSON.toJson(jObj);
  }

  private static String status(boolean flag, int next) {
    JsonObject jObj = new JsonObject();
    jObj.addProperty("success", flag);
    jObj.addProperty("next_player", next);
    return GSON.toJson(jObj);
  }
}
