package edu.brown.cs.blokus.handlers;

import java.util.Collections;

import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles index page in web GUI.
  */
public class IndexHandler implements TemplateViewRoute {
  private final Database db;

  /**
    * Creates a new index handler.
    * @param db a reference to the database
    */
  public IndexHandler(Database db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    if (db.getUserId(req.cookie("session")) != null) {
      res.redirect("/auth/main");
      return null;
    }

    return new ModelAndView(Collections.emptyMap(), "index.ftl");
  }
}
