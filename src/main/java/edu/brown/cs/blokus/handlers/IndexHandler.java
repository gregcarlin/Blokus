package edu.brown.cs.blokus.handlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;


/**
  * Handles main page in web GUI.
  */
public class IndexHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request req, Response res) {
    Map<String, Object> variables =
      ImmutableMap.of("title", "Autocorrect");
    return new ModelAndView(variables, "action.ftl");
  }
}
