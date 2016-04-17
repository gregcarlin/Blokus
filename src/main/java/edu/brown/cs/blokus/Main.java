package edu.brown.cs.blokus;

import java.io.File;
import java.io.IOException;

import edu.brown.cs.blokus.db.Database;
import edu.brown.cs.blokus.handlers.AuthHandler;
import edu.brown.cs.blokus.handlers.ExceptionPrinter;
import edu.brown.cs.blokus.handlers.IndexHandler;
import edu.brown.cs.blokus.handlers.JoinHandler;
import edu.brown.cs.blokus.handlers.LiveUpdater;
import edu.brown.cs.blokus.handlers.LoginHandler;
import edu.brown.cs.blokus.handlers.MainHandler;
import edu.brown.cs.blokus.handlers.MoveHandler;
import edu.brown.cs.blokus.handlers.NewGameHandler;
import edu.brown.cs.blokus.handlers.PlayHandler;
import edu.brown.cs.blokus.handlers.SignoutHandler;
import edu.brown.cs.blokus.handlers.SignupHandler;

import freemarker.template.Configuration;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

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
    OptionSpec<String> dbHostSpec = parser
      .accepts("dbhost", "The host address of the database to use.")
      .withRequiredArg().ofType(String.class)
      .defaultsTo(Database.DEFAULT_HOST);
    OptionSpec<Integer> dbPortSpec = parser
      .accepts("dbport", "The port the database to use is running on.")
      .withRequiredArg().ofType(Integer.class)
      .defaultsTo(Database.DEFAULT_PORT);
    OptionSpec<String> dbSpec = parser
      .accepts("db", "The name of the database to use.")
      .withRequiredArg().ofType(String.class)
      .defaultsTo(Database.DEFAULT_DB);
    OptionSet options = parser.parse(args);

    runSparkServer(options.valueOf(portSpec), options.valueOf(dbHostSpec),
        options.valueOf(dbPortSpec), options.valueOf(dbSpec));
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

  private void runSparkServer(int port,
      String dbHost, int dbPort, String dbName) {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.port(port);

    FreeMarkerEngine freeMarker = createEngine();
    Database db = new Database(dbHost, dbPort, dbName);

    Spark.webSocket("/live", LiveUpdater.class);

    // Setup Spark Routes
    Spark.get("/", new IndexHandler(), freeMarker);
    Spark.post("/login", new LoginHandler(db), freeMarker);
    Spark.post("/signup", new SignupHandler(db), freeMarker);
    Spark.get("/signout", new SignoutHandler(), freeMarker);
    Spark.before("/auth/*", new AuthHandler(db));
    Spark.get("/auth/main", new MainHandler(db), freeMarker);
    Spark.post("/auth/new", new NewGameHandler(db), freeMarker);
    Spark.get("/auth/join/:id", new JoinHandler(db), freeMarker);
    Spark.get("/auth/play/:id", new PlayHandler(db), freeMarker);
    Spark.post("/auth/move/:id", new MoveHandler(db));
  }
}
