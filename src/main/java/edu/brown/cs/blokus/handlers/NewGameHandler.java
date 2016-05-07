package edu.brown.cs.blokus.handlers;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.db.Datasource;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles creation of new games.
  */
public class NewGameHandler implements TemplateViewRoute {
  private static final ImmutableMap<String, GameSettings.Type> TYPE_MAP =
    ImmutableMap.of("public", GameSettings.Type.PUBLIC,
        "private", GameSettings.Type.PRIVATE,
        "local", GameSettings.Type.LOCAL);
  private final Datasource db;

  /**
    * Creates a new handler for making new games.
    * @param db a reference to the database
    */
  public NewGameHandler(Datasource db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final QueryParamsMap qm = req.queryMap();
    final GameSettings.Type type = TYPE_MAP.get(qm.value("type"));
    final int count = Integer.parseInt(qm.value("count"));
    final String rawTimer = qm.value("timer");
    try {
      final int timer = rawTimer == null || rawTimer.isEmpty()
          ? 0 : Integer.parseInt(rawTimer);
      if (timer < 0 || timer > Math.pow(2, 31) / 1000) {
        res.redirect("/auth/main?error=That timer is too large!");
        return null;
      }

      GameSettings settings = new GameSettings.Builder()
        .type(type)
        .state(GameSettings.State.UNSTARTED)
        .maxPlayers(count)
        .timer(timer)
        .build();
      settings.addPlayer(req.attribute("user-id"));
      Game game = new Game.Builder()
        .setSettings(settings)
        .build();

      String id = db.saveGame(game);
      res.redirect("/auth/play/" + id);

      return null;
    } catch (NumberFormatException e) {
      res.redirect("/auth/main?error=That timer is too large!");
      return null;
    }
  }
}
