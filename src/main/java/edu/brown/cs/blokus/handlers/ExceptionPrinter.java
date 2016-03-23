package edu.brown.cs.blokus.handlers;

import java.io.PrintWriter;
import java.io.StringWriter;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;


/**
  * Handles printing of exceptions to web GUI users.
  */
public class ExceptionPrinter implements ExceptionHandler {
  @Override
  public void handle(Exception e, Request req, Response res) {
    res.status(HTTP.ERROR);
    StringWriter stacktrace = new StringWriter();
    try (PrintWriter pw = new PrintWriter(stacktrace)) {
      pw.println("<pre>");
      e.printStackTrace(pw);
      pw.println("</pre>");
    }
    res.body(stacktrace.toString());
  }
}
