package edu.brown.cs.blokus.db;

import java.util.List;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;


/**
  * A source of data.
  */
public interface Datasource extends AutoCloseable {
  /**
    * Creates a new user with a given username and password.
    * @param user the username
    * @param pass the password
    * @return the id of the new user, of null if this username already exists
    */
  String createUser(String user, String pass);

  /**
    * Gets the id of a user given valid credentials.
    * @param user the username
    * @param pass the password
    * @return the id of the user if credentials are valid, null otherwise
    */
  String getUserId(String user, String pass);

  /**
    * Creates a new session for a given user.
    * @param userId the id of the user logging in
    * @return the session hash
    */
  String logIn(String userId);

  /**
    * Gets the user id associated with a session hash.
    * @param hash the session hash provided by the client
    * @return the id of the associated user, or null if none
    */
  String getUserId(String hash);

  /**
    * Gets the name of a user.
    * @param id the id of the user to get
    * @return the user's name as a string, or null if invalid id
    */
  String getName(String id);

  /**
    * Gets a raw game with a given id.
    * @param id the id of the game to find
    * @return the game's json, or null if not found
    */
  String getGameRaw(String id);

  /**
    * Gets a game with a given id.
    * @param id the id of the game to find
    * @return the game, or null if not found
    */
  Game getGame(String id);

  /**
    * Saves a game to the database.
    * If a game with a matching id is already present, it is replaced.
    * @param game the game to save
    * @return the id of the saved game
    */
  String saveGame(Game game);

  /**
    * Gets a list of games that are public and haven't been started yet.
    * @param without the id of the player to exclude from the results
    * @return a set of game settings representing each game
    */
  List<GameSettings> getOpenGames(String without);

  /**
    * Gets all games that a given player has joined.
    * Excludes games that are finished.
    * @param playerId the unique id of the player
    * @return a set of game settings representing each game
    */
  List<GameSettings> getGamesWith(String playerId);
}
