package edu.brown.cs.blokus.handlers;

import java.util.Collections;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles the display of the play page.
  */
public class PlayHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request req, Response res) {
    return new ModelAndView(Collections.emptyMap(), "play.ftl");
  }
}
