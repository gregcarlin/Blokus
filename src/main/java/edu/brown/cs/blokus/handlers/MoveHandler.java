package edu.brown.cs.blokus.handlers;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Orientation;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.db.Database;
import edu.brown.cs.parse.BodyParser;

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
    final String gameId = req.params("id");
    synchronized (gameId.intern()) {
      final Game game = db.getGame(gameId/*, true*/);

      // if it's not this user's turn
      if (!user.equals(game.getPlayer(game.getTurn()).getId())) {
        db.saveGame(game);
        return FAIL;
      }

      BodyParser body = new BodyParser(req.body());
      Shape shape = Shape.values()[body.getInt("piece")];
      Orientation orientation
        = Orientation.values()[body.getInt("orientation")];
      int x = body.getInt("x");
      int y = body.getInt("y");
      Move move = new Move(shape, orientation, x, y);

      // if it's an illegal move
      if (!game.isLegal(move)) {
        db.saveGame(game);
        return FAIL;
      }

      game.makeMove(move);
      db.saveGame(game);
      return status(true, game.getTurn().ordinal());
    }
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
