package edu.brown.cs.blokus.handlers;

import edu.brown.cs.blokus.db.Datasource;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;


/**
  * Ensures that the user making the request is properly authenticated.
  * If not, they are redirected.
  */
public class AuthHandler implements Filter {
  private final Datasource db;

  /**
    * Creates a new authentication handler.
    * @param db a reference to the database
    */
  public AuthHandler(Datasource db) {
    this.db = db;
  }

  @Override
  public void handle(Request req, Response res) {
    String userId = db.getUserId(req.cookie("session"));
    req.attribute("user-id", userId);
    if (userId == null) {
      res.redirect("/?error=You must be logged in to visit that page.&dest="
          + req.pathInfo());
      Spark.halt(HTTP.UNAUTHORIZED,
          "You must be logged in to visit this page.");
    }
  }
}
