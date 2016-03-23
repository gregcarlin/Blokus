package edu.brown.cs.blokus.handlers;

import java.util.Collections;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles index page in web GUI.
  */
public class IndexHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request req, Response res) {
    return new ModelAndView(Collections.emptyMap(), "index.ftl");
  }
}
