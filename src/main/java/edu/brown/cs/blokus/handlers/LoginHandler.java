package edu.brown.cs.blokus.handlers;

import edu.brown.cs.blokus.db.Datasource;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles user logins.
  */
public class LoginHandler implements TemplateViewRoute {
  private final Datasource db;

  /**
    * Creates a new handler for user logins.
    * @param db a reference to the database
    */
  public LoginHandler(Datasource db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    final QueryParamsMap qm = req.queryMap();
    final String user = qm.value("username");
    final String pass = qm.value("password");
    final String dest = qm.value("dest");

    if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
      res.redirect("/?error=All fields are required.&hide=register");
      return null;
    }

    String userId = db.getUserId(user, pass);
    if (userId == null) {
      res.redirect(
          "/?error=Your username or password is incorrect.&hide=register");
      return null;
    }

    String hash = db.logIn(userId);
    res.cookie("session", hash);
    res.redirect(dest == null ? "/auth/main" : dest);

    return null;
  }
}
