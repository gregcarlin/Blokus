package edu.brown.cs.blokus.handlers;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.db.Datasource;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles the joining of games.
  */
public class JoinHandler implements TemplateViewRoute {
  private final Datasource db;

  /**
    * Creates a new handler that adds players to games.
    * @param db a reference to the database
    */
  public JoinHandler(Datasource db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final String userId = req.attribute("user-id");
    final String gameId = req.params("id");
    synchronized (gameId.intern()) {
      final Game game = db.getGame(gameId);

      // if user is already in this game, just redirect them to the actual game
      for (Player player : game.getAllPlayers()) {
        if (player.getId().equals(userId)) {
          res.redirect("/auth/play/" + gameId);
          return null;
        }
      }

      game.getSettings().addPlayer(userId);
      String newId = db.saveGame(game);
      assert newId.equals(gameId);

      res.redirect("/auth/play/" + newId);
      return null;
    }
  }
}
