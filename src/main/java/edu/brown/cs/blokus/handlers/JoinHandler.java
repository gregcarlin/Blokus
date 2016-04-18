package edu.brown.cs.blokus.handlers;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles the joining of games.
  */
public class JoinHandler implements TemplateViewRoute {
  private final Database db;

  /**
    * Creates a new handler that adds players to games.
    * @param db a reference to the database
    */
  public JoinHandler(Database db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final String userId = req.attribute("user-id");
    final String gameId = req.params("id");
    final Game game = db.getGame(gameId);

    game.getSettings().addPlayer(userId);
    String newId = db.saveGame(game);
    assert newId.equals(gameId);

    res.redirect("/auth/play/" + newId);
    return null;
  }
}
