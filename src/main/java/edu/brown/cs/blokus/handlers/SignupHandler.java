package edu.brown.cs.blokus.handlers;

import edu.brown.cs.blokus.db.Database;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles user logins.
  */
public class SignupHandler implements TemplateViewRoute {
  private final Database db;

  /**
    * Creates a new handler for user sign ups.
    * @param db a reference to the database
    */
  public SignupHandler(Database db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final QueryParamsMap qm = req.queryMap();
    final String user = qm.value("username");
    final String pass = qm.value("password");

    if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
      // TODO report empty data
      return null;
    }

    if (user.length() > 15) {
      // TODO report oversized username
      return null;
    }

    String userId = db.createUser(user, pass);
    if (userId == null) {
      // TODO report user with same username
      return null;
    }

    String hash = db.logIn(userId);
    res.cookie("session", hash);
    res.redirect("/auth/main");

    return null;
  }
}
