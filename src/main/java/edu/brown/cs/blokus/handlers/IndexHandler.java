package edu.brown.cs.blokus.handlers;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.blokus.db.Datasource;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles index page in web GUI.
  */
public class IndexHandler implements TemplateViewRoute {
  private final Datasource db;

  /**
    * Creates a new index handler.
    * @param db a reference to the database
    */
  public IndexHandler(Datasource db) {
    this.db = db;
  }

  @Override
  public ModelAndView handle(Request req, Response res) {
    if (db.getUserId(req.cookie("session")) != null) {
      res.redirect("/auth/main");
      return null;
    }

    final String error = req.queryParams("error");
    final String hide = req.queryParams("hide");
    String dest = req.queryParams("dest");
    dest = (dest == null || dest.isEmpty()) ? "/auth/main" : dest;
    ImmutableMap.Builder<String, String> mapBuilder
      = new ImmutableMap.Builder<String, String>()
        .put("dest", dest)
        .put("hide", hide == null ? "" : hide);
    if (error != null && !error.isEmpty()) {
      mapBuilder.put("error", error);
    }
    return new ModelAndView(mapBuilder.build(), "index.ftl");
  }
}
