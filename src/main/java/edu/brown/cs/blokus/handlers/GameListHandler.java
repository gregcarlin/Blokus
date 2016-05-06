package edu.brown.cs.blokus.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.db.Datasource;

import spark.Request;
import spark.Response;
import spark.Route;


/**
  * Handles the ajax request a client sends to get information about
  * listed games.
  */
public class GameListHandler implements Route {
  private static final Gson GSON = new Gson();

  private final Datasource db;

  /**
    * Creates a new handler that handles game list requests.
    * @param db a reference to the database
    */
  public GameListHandler(Datasource db) {
    this.db = db;
  }

  private JsonObject toJson(GameSettings gs) {
    JsonObject jObj = new JsonObject();
    jObj.addProperty("_id", gs.getId());
    jObj.addProperty("state", gs.getState().ordinal());

    JsonObject params = new JsonObject();
    params.addProperty("privacy", gs.getType().ordinal());
    params.addProperty("num_players", gs.getMaxPlayers());
    params.addProperty("timer", gs.getHumanTimer());
    jObj.add("params", params);

    JsonArray players = new JsonArray();
    for (String player : gs.getUniquePlayers()) {
      players.add(db.getName(player));
    }
    jObj.add("players", players);

    return jObj;
  }

  @Override
  public Object handle(Request req, Response res) {
    final String user = req.attribute("user-id");

    JsonArray jPublic = new JsonArray();
    for (GameSettings gs : db.getOpenGames(user)) {
      jPublic.add(toJson(gs));
    }

    JsonArray jCurrent = new JsonArray();
    for (GameSettings gs : db.getGamesWith(user)) {
      jCurrent.add(toJson(gs));
    }

    JsonObject jObj = new JsonObject();
    jObj.add("public", jPublic);
    jObj.add("current", jCurrent);
    return GSON.toJson(jObj);
  }
}
