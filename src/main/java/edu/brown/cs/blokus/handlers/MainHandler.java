package edu.brown.cs.blokus.handlers;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles the display of the main page.
  */
public class MainHandler implements TemplateViewRoute {
  private final Database db;

  /**
    * Creates an instance of a handler that displays the main page.
    * @param db a reference to the database
    */
  public MainHandler(Database db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final String user = req.attribute("user-id");
    return new ModelAndView(ImmutableMap.of(
          "openGames", db.getOpenGames(0, user),
          "currGames", db.getGamesWith(user)),
        "main.ftl");
  }
}
