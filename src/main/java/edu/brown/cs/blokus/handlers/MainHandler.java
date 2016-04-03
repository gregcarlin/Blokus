package edu.brown.cs.blokus.handlers;

import java.util.Collections;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles the display of the main page.
  */
public class MainHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request req, Response res) {
    return new ModelAndView(Collections.emptyMap(), "play.ftl");
  }
}
