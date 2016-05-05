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
  private static final String[] TIPS = {
    "Try to play your biggest pieces first, but don’t be afraid to put a small piece in a hole to access a new area.",
    "In the beginning of the game, try to get to the center of the board as fast as possible to increase the number of areas that you will have access to later.",
    "In a two player game, try to secure an area that both of your colors have access to, but no opponent color does.",
    "In a 4 player game, try to prioritizing increasing your own color’s options rather than blocking an opponent.",
    "At the end of the game, be sure to play in places that an opponent could block before playing in your secured areas.",
    "<img src = \"http://pentolla.com/images/Pieces.png\"></img>"
  };
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
    return new ModelAndView(
        ImmutableMap.of("username", db.getName(user),
          "tip", TIPS[(int) (Math.random() * TIPS.length)]),
        "main.ftl");
  }
}
