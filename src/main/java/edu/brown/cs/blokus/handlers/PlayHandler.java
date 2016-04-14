package edu.brown.cs.blokus.handlers;

import java.util.Collections;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
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
    final Game game = db.getGame(req.params("id"));

    // TODO load data from game into view
    return new ModelAndView(Collections.emptyMap(), "play.ftl");
  }
}
