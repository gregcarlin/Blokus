package edu.brown.cs.blokus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.blokus.handlers.IndexHandler;

import freemarker.template.Configuration;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;


/**
  * The main class for the blokus project.
  * Handles the basic parsing of arguments, reading of input,
  * and organization of output.
  * Contains the main method.
  */
public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private static final String DEFAULT_DB = "blokus";
  private static final int HTTP_ERROR = 500;

  /**
    * The main method.
    * @param args the arguments supplied to the program at the command line
    */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private final String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    OptionParser parser = new OptionParser();

    OptionSpec<Integer> portSpec = parser
      .accepts("port", "The port the GUI should run on.")
      .withRequiredArg().ofType(Integer.class)
      .defaultsTo(DEFAULT_PORT);
    OptionSpec<String> dbSpec = parser
      .accepts("db", "The name of the database to use.")
      .withRequiredArg().ofType(String.class)
      .defaultsTo(DEFAULT_DB);
    OptionSet options = parser.parse(args);

    final int port = options.valueOf(portSpec);
    final String dbName = options.valueOf(dbSpec);
    runSparkServer(port, dbName);
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port, String dbName) {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.setPort(port);

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/", new IndexHandler(), freeMarker);
  }

  /**
    * Handles printing of exceptions to web GUI users.
    */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(HTTP_ERROR);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }


}
