package edu.brown.cs.blokus.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.Game;
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
    Game rich = db.getGame(gameId);
    db.saveGame(rich);

    JsonObject game
      = GSON.fromJson(db.getGameRaw(gameId), JsonObject.class);
    game.addProperty("_id",
        game.get("_id").getAsJsonObject().get("$oid").getAsString());
    game.addProperty("loaded_by", user);
    JsonArray players = game.getAsJsonArray("players");
    final int len = players.size();
    for (int i = 0; i < len; i++) {
      JsonObject player = players.get(i).getAsJsonObject();
      String id = player.get("_id").getAsJsonObject().get("$oid").getAsString();
      player.addProperty("_id", id);
      player.addProperty("name", db.getName(id));
      players.set(i, player);
    }
    game.add("players", players);

    return GSON.toJson(game);
  }
}
