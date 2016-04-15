package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Game settings.
 *
 * @author aaronzhang
 */
public class GameSettings {
  /**
    * Represents the type of a game.
    */
  public static enum Type {
    PUBLIC, // anyone can join
    PRIVATE, // this game is unlisted
    LOCAL // this game is run from one computer
  }

  /**
    * Represents the state of a game.
    */
  public static enum State {
    UNSTARTED, // people are still joining the game
    PLAYING, // game is currently being played
    FINISHED // game is over
  }

  private final String id;
  private final Map<Turn, Player> players;
  private Type type = Type.PUBLIC;
  private State state = State.UNSTARTED;
  private int maxPlayers = 4;
  private int timer = 0;


  /**
    * Game settings builder.
    */
  public static class Builder {
    private final GameSettings settings;

    /**
      * Creates a new game settings builder for a game that hasn't
      * been saved yet.
      */
    public Builder() {
      this(null);
    }

    /**
      * Creates a new game settings builder.
      * @param id the id of the associated game
      */
    public Builder(String id) {
      this.settings = new GameSettings(id);
    }

    /**
      * Sets the type of this game.
      * @param type the game type
      * @return this builder
      */
    public Builder type(Type type) {
      settings.type = type;
      return this;
    }

    /**
      * Sets the sate of this game.
      * @param state the game state
      * @return this builder
      */
    public Builder state(State state) {
      settings.state = state;
      return this;
    }

    /**
      * Sets the maximum number of allowed players in the game.
      * Note: should be either 2 or 4
      * @return this builder
      */
    public Builder maxPlayers(int maxPlayers) {
      if (maxPlayers != 2 && maxPlayers != 4) {
        throw new IllegalArgumentException(
            String.format("%d player games are not supported.", maxPlayers));
      }
      settings.maxPlayers = maxPlayers;
      return this;
    }

    /**
      * Sets the game timer.
      * 0 represents no timer.
      * @return this builder
      */
    public Builder timer(int timer) {
      if (timer < 0) {
        throw new IllegalArgumentException("Timer cannot be negative.");
      }
      settings.timer = timer;
      return this;
    }

    /**
     * Sets the given player. The player is specified by turn: first, second,
     * third, or fourth. If a player is not set with this method, the player
     * defaults to a new player.
     *
     * @param turn which player to set
     * @param player the player
     * @return this builder
     */
    public Builder player(Turn turn, Player player) {
      settings.players.put(turn, player);
      return this;
    }

    public GameSettings build() {
      if (settings.players.size() == 0) {
        throw new IllegalStateException("Game must have at least one player.");
      }
      if (settings.maxPlayers == 2) {
        player(Turn.THIRD, settings.players.get(Turn.FIRST));
        player(Turn.FOURTH, settings.players.get(Turn.SECOND));
      }
      return settings;
    }
  }

  /**
    * Creates a new game settings.
    * @param id the id of the associated game
    */
  private GameSettings(String id) {
    this.players = new EnumMap<>(Turn.class);
    this.id = id;
  }

  /**
    * Gets the id of the associated game.
    * @return the string id
    */
  public String getId() {
    return id;
  }

  /**
    * Whether or not the associated game has been assigned an id.
    * @return true if it has an id, false otherwise
    */
  public boolean hasId() {
    return id != null;
  }

  /**
    * Gets the type of this game.
    * @return the game type
    */
  public Type getType() {
    return type;
  }

  /**
    * Gets the state of this game.
    * @return the game state
    */
  public State getState() {
    return state;
  }

  /**
    * Gets the maximum number of players allowed in this game.
    * @return the max number of players
    */
  public int getMaxPlayers() {
    return maxPlayers;
  }

  /**
    * Gets the maximum time a player is allowed before making a move.
    * @return the max allowed time, or 0 if this game has no timer
    */
  public int getTimer() {
    return timer;
  }

  /**
   * Gets the specified player.
   *
   * @param turn turn
   * @return player
   */
  public Player getPlayer(Turn turn) {
    return players.get(turn);
  }

  /**
    * Gets all players in this game in turn order.
    * @return an ordered list of all players
    */
  public List<Player> getAllPlayers() {
    List<Player> rt = new ArrayList<>();
    for (Turn turn : Turn.values()) {
      if (players.containsKey(turn)) {
        rt.add(players.get(turn));
      }
    }
    return rt;
  }

  /**
    * Adds a new player to the game.
    * @param id the id of the user
    */
  public void addPlayer(String id) {
    if (getState() != State.UNSTARTED) {
      throw new IllegalStateException("Players can't join a game in progress.");
    }

    for (int i = 0; i < maxPlayers; i++) {
      Turn turn = Turn.values()[i];
      if (!players.containsKey(turn)) {
        Player player = new Player(id);
        players.put(turn, player);
        if (maxPlayers == 2) {
          players.put(turn.next().next(), player);
        }
        return;
      }
    }

    throw new IllegalStateException("Game is already full.");
  }
}
