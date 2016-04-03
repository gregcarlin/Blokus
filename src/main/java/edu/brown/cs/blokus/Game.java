package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A game of Blokus. A game has a board and players. Games should be constructed
 * with {@link Builder}.
 *
 * @author aaronzhang
 */
public class Game {

  /**
   * Board.
   */
  private Board board;

  /**
   * Players.
   */
  private final Map<Turn, Player> players;

  /**
   * Current turn.
   */
  private Turn turn;

  private long lastTurnTime;

  /**
    * Game settings.
    */
  private GameSettings settings;

  /**
   * Game builder.
   */
  public static class Builder {

    /**
     * Game being constructed.
     */
    private final Game game;

    /**
     * Default board size.
     */
    private static final int DEFAULT_SIZE = 20;

    /**
     * Instantiates a game builder.
     */
    public Builder() {
      this.game = new Game();
    }

    /**
     * Sets the grid. An empty square has the value 0; an occupied square has
     * the number of the player that occupies it. Each inner array is a row. If
     * this method is not called, the default grid is an empty grid with side
     * length {@value Builder#DEFAULT_SIZE}.
     *
     * @param grid grid
     * @return this builder
     */
    public Builder setGrid(int[][] grid) {
      game.board = new Board(grid);
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
    public Builder setPlayer(Turn turn, Player player) {
      game.players.put(turn, player);
      return this;
    }

    /**
     * Sets turn. If this method is not called, defaults to {@link Turn#FIRST}.
     *
     * @param turn turn
     * @return this builder
     */
    public Builder setTurn(Turn turn) {
      game.turn = turn;
      return this;
    }

    /**
      * Sets the last turn time.
      * @param lastTurnTime the time of last turn
      * @return this builder
      */
    public Builder setLastTurnTime(long lastTurnTime) {
      game.lastTurnTime = lastTurnTime;
      return this;
    }

    /**
      * Sets game settings.
      * This method must be called.
      * @param settings game settings
      * @return this builder
      */
    public Builder setSettings(GameSettings settings) {
      game.settings = settings;
      return this;
    }

    /**
     * Builds game.
     *
     * @return game
     */
    public Game build() {
      if (game.board == null) {
        game.board = new Board(DEFAULT_SIZE);
      }
      for (Turn turn : Turn.values()) {
        game.players.putIfAbsent(turn, new Player());
      }
      if (game.turn == null) {
        game.turn = Turn.FIRST;
      }
      if (game.settings == null) {
        throw new IllegalStateException("Game settings must be set.");
      }
      return game;
    }
  }

  /**
   * New game.
   */
  private Game() {
    this.players = new EnumMap<>(Turn.class);
  }

  /**
   * Whether the move is legal.
   *
   * @param move move
   * @return whether the move is legal
   */
  public boolean isLegal(Move move) {
    /*
     TODO--I'll implement this sometime this week
     */
    return false;
  }

  /**
   * Makes a move. If the turn player has the shape used by the move, updates
   * the board and removes the shape from the turn player's remaining pieces.
   * Regardless, advances to the next turn.
   *
   * @param move move
   */
  public void makeMove(Move move) {
    if (getPlayer(turn).hasPiece(move.getShape())) {
      for (Square moveSquare : move.getSquares()) {
        board.setXY(moveSquare.getX(), moveSquare.getY(), turn.mark());
      }
      getPlayer(turn).removePiece(move.getShape());
    }
    pass();
  }

  /**
   * Advances to the next turn.
   */
  public void pass() {
    turn = turn.next();
  }

  /**
   * Whether the player can move. If not, the player's
   * {@link Player#stopPlaying()} method will be called.
   *
   * @param turn
   * @return
   */
  public boolean canMove(Turn turn) {
    /*
     TODO--I'll work on this after implementing isLegal()
     */
    return false;
  }

  /**
   * Gets the grid of the board.
   *
   * @return grid
   */
  public int[][] getGrid() {
    return board.getGrid();
  }

  /**
    * Gets the grid of the board as a 2d list.
    * @return a list of lists
    */
  public List<List<Integer>> getGridAsList() {
    List<List<Integer>> rt = new ArrayList<>();
    int[][] grid = getGrid();
    for (int[] row : grid) {
      List<Integer> rowLs = new ArrayList<>();
      for (int i : row) {
        rowLs.add(i);
      }
      rt.add(rowLs);
    }
    return rt;
  }

  /**
   * Gets value at row and column in grid.
   *
   * @param row row
   * @param column column
   * @return value
   */
  public int getRowColumn(int row, int column) {
    return board.getRowColumn(row, column);
  }

  /**
   * Gets value at x and y-coordinates in grid.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return value
   */
  public int getXY(int x, int y) {
    return board.getXY(x, y);
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
   * @return current turn
   */
  public Turn getTurn() {
    return turn;
  }

  /**
    * Gets the timestamp of the last turn.
    * @return the number of milliseconds elapsed between the epoch
    * and the last turn
    */
  public long getLastTurnTime() {
    return lastTurnTime;
  }

  /**
    * Gets the settings of this game.
    * @return the game settings
    */
  public GameSettings getSettings() {
    return settings;
  }
}
