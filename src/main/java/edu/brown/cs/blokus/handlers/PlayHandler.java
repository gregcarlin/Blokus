package edu.brown.cs.blokus.handlers;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;


/**
  * Handles the display of the play page.
  */
public class PlayHandler implements TemplateViewRoute {
  private final Database db;

  /**
    * Creates a new handler that displays the play page.
    * @param db a reference to the database
    */
  public PlayHandler(Database db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final String user = req.attribute("user-id");
    final Game game = db.getGame(req.params("id"));

    if (!game.getAllPlayers().contains(user)) {
      Spark.halt(HTTP.UNAUTHORIZED,
          "You are not a member of this game.");
    }

    return new ModelAndView(ImmutableMap.of("game", game), "play.ftl");
  }
}
