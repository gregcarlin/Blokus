package edu.brown.cs.blokus.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import edu.brown.cs.blokus.db.Database;

import spark.Request;
import spark.Response;
import spark.Route;


/**
  * Handles the ajax request a client sends to get information about a game.
  */
public class InfoHandler implements Route {
  private static final Gson GSON = new Gson();

  private final Database db;

  /**
    * Creates a new handler that handles info requests.
    * @param db a reference to the database
    */
  public InfoHandler(Database db) {
    this.db = db;
  }

  @Override
  public Object handle(Request req, Response res) {
    final String user = req.attribute("user-id");
    final String gameId = req.params("id");

    // get and save game so timer is updated
    synchronized (gameId.intern()) {
      Game rich = db.getGame(gameId);
      db.saveGame(rich);

      JsonObject game
        = GSON.fromJson(db.getGameRaw(gameId), JsonObject.class);

      game.addProperty("_id",
          game.get("_id").getAsJsonObject().get("$oid").getAsString());
      game.addProperty("loaded_by", user);
      game.addProperty("sent", System.currentTimeMillis());

      JsonArray players = game.getAsJsonArray("players");
      final int len = players.size();
      for (int i = 0; i < len; i++) {
        JsonElement oPlayer = players.get(i);
        if (oPlayer == null || oPlayer instanceof JsonNull) {
          players.set(i, null);
        } else {
          JsonObject player = oPlayer.getAsJsonObject();
          String id
            = player.get("_id").getAsJsonObject().get("$oid").getAsString();
          player.addProperty("_id", id);
          player.addProperty("name", db.getName(id));

          JsonArray playable = new JsonArray();
          for (Square sq : rich.playableCorners(Turn.values()[i])) {
            JsonObject jSq = new JsonObject();
            jSq.addProperty("x", sq.getX());
            jSq.addProperty("y", sq.getY());
            playable.add(jSq);
          }
          player.add("playable", playable);

          players.set(i, player);
        }
      }
      game.add("players", players);

      return GSON.toJson(game);
    }
  }
}
