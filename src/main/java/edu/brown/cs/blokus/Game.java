package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import edu.brown.cs.blokus.handlers.LiveUpdater;
import java.util.HashSet;
import java.util.Set;


/**
 * A game of Blokus. A game has a board and players. Games should be constructed
 * with {@link Builder}.
 */
public class Game {

  /**
   * Board.
   */
  private Board board;

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
     * Instantiates a game builder.
     */
    public Builder() {
      this.game = new Game();
    }

    /**
     * Sets the grid. An empty square has the value 0; an occupied square has
     * the number of the player that occupies it. Each inner array is a row. If
     * neither this method nor {@link Builder#setBoard(Board)} is called, then
     * the default grid is an empty grid with side length
     * {@value Builder#DEFAULT_SIZE}.
     *
     * @param grid grid
     * @return this builder
     */
    public Builder setGrid(int[][] grid) {
      game.board = new Board(grid);
      return this;
    }
    
    /**
     * Sets the board. An alternative to {@link Builder#setGrid(int[][])},
     * possibly useful in testing if a board has already been built using Board
     * methods. If neither this method nor {@link Builder#setBoard(Board)} is
     * called, then the default grid is an empty grid with side length
     * {@value Builder#DEFAULT_SIZE}.
     * 
     * @param board board
     * @return this builder
     */
    public Builder setBoard(Board board) {
      game.board = board;
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
        game.board = new Board(Board.DEFAULT_SIZE);
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
  }

  /**
   * Whether the move is legal. For a move to be legal, the turn player must
   * have the piece being played. All the squares that the move covers must be
   * unoccupied squares on the board. No square in the move can share an edge
   * with a square already on the board with the same color. At least one square
   * in the move must share a corner with a square already on the board with the
   * same color.
   *
   * @param move move
   * @return whether the move is legal
   */
  public boolean isLegal(Move move) {
    // Turn player must have the piece being played
    if (!getPlayer(turn).hasPiece(move.getShape())) {
      return false;
    }
    
    // All squares in the move must be unoccupied squares on the board
    for (Square square : move.getSquares()) {
      if (!(square.getX() >= 0
          && square.getX() < board.size()
          && square.getY() >= 0
          && square.getY() < board.size()
          && board.getXY(square.getX(), square.getY()) == 0)) {
        return false;
      }
    }

    // If this is the first turn, the move must cover the player's corner
    // TODO make this work
    /*if (firstMove(turn)) {
      Square corner = getCorner(turn);
      return move.getSquares().contains(corner);
    }*/
    
    // Get the squares that share an edge or corner with any square in the move
    Set<Square> edges = new HashSet<>();
    Set<Square> corners = new HashSet<>();
    for (Square square : move.getSquares()) {
      // Which directions to check (don't check if we would go off the board)
      boolean checkLeft = square.getX() > 0;
      boolean checkRight = square.getX() < board.size() - 1;
      boolean checkDown = square.getY() > 0;
      boolean checkUp = square.getY() < board.size() - 1;
      // Squares that share an edge with a square in the move
      if (checkLeft) {
        edges.add(square.translate(-1, 0));
      }
      if (checkRight) {
        edges.add(square.translate(1, 0));
      }
      if (checkDown) {
        edges.add(square.translate(0, -1));
      }
      if (checkUp) {
        edges.add(square.translate(0, 1));
      }
      // Squares that share a corner with a square in the move
      if (checkLeft && checkUp) {
        corners.add(square.translate(-1, 1));
      }
      if (checkRight && checkUp) {
        corners.add(square.translate(1, 1));
      }
      if (checkRight && checkDown) {
        corners.add(square.translate(1, -1));
      }
      if (checkLeft && checkDown) {
        corners.add(square.translate(-1, -1));
      }
    }
    corners.removeAll(edges);
    edges.removeAll(move.getSquares());
    
    // No squares in the move can share an edge with the player's pieces
    if (edges.stream().anyMatch(s -> board.getSquare(s) == turn.mark())) {
      return false;
    }
    // Some square in the move must share a corner with the player's pieces
    if (corners.stream().noneMatch(s -> board.getSquare(s) == turn.mark())) {
      return false;
    }
    
    return true;
  }

  /**
   * Whether the square is a playable corner for the player.
   *
   * @param square square
   * @param turn turn
   * @return whether square is a playable corner
   */
  private boolean isCorner(Square square, Turn turn) {
    Set<Square> edges = new HashSet<>();
    Set<Square> corners = new HashSet<>();
    // Which directions to check (don't check if we would go off the board)
    boolean checkLeft = square.getX() > 0;
    boolean checkRight = square.getX() < board.size() - 1;
    boolean checkDown = square.getY() > 0;
    boolean checkUp = square.getY() < board.size() - 1;
    // Squares that share an edge with a square in the move
    if (checkLeft) {
      edges.add(square.translate(-1, 0));
    }
    if (checkRight) {
      edges.add(square.translate(1, 0));
    }
    if (checkDown) {
      edges.add(square.translate(0, -1));
    }
    if (checkUp) {
      edges.add(square.translate(0, 1));
    }
    // Squares that share a corner with a square in the move
    if (checkLeft && checkUp) {
      corners.add(square.translate(-1, 1));
    }
    if (checkRight && checkUp) {
      corners.add(square.translate(1, 1));
    }
    if (checkRight && checkDown) {
      corners.add(square.translate(1, -1));
    }
    if (checkLeft && checkDown) {
      corners.add(square.translate(-1, -1));
    }
    corners.removeAll(edges);

    // No squares in the move can share an edge with the player's pieces
    if (edges.stream().anyMatch(s -> board.getSquare(s) == turn.mark())) {
      return false;
    }
    // Some square in the move must share a corner with the player's pieces
    if (corners.stream().noneMatch(s -> board.getSquare(s) == turn.mark())) {
      return false;
    }
    return true;
  }

  /**
   * Returns {@code true} if the player has not moved yet.
   *
   * @param turn turn
   * @return whether player has not moved yet
   */
  private boolean firstMove(Turn turn) {
    return getPlayer(turn).getRemainingPieces().size() == Shape.NUM_SHAPES;
  }

  /**
   * Gets the player's corner square.
   *
   * @param turn turn
   * @return corner square
   */
  private Square getCorner(Turn turn) {
    switch (turn) {
      case FIRST:
        return new Square(0, board.size() - 1);
      case SECOND:
        return new Square(board.size() - 1, board.size() - 1);
      case THIRD:
        return new Square(board.size() - 1, 0);
      case FOURTH:
        return new Square(0, 0);
      default:
        throw new AssertionError("invalid turn");
    }
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
      board.makeMove(move, turn.mark());
      getPlayer(turn).removePiece(move.getShape());
    }
    LiveUpdater.moveMade(this, move);
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
    TODO--I'll work on this next
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

  /**
   * Gets the specified player.
   *
   * @param turn turn
   * @return player
   */
  public Player getPlayer(Turn turn) {
    return getSettings().getPlayer(turn);
  }

  /**
    * Gets all players in this game.
    * @return a collection of all players
    */
  public Collection<Player> getAllPlayers() {
    return getSettings().getAllPlayers();
  }

  public static void main(String[] args) {
    Board b = new Board(20);
    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 20; y++) {
        if (Math.random() > 0.5) {
          b.setXY(x, y, 0);
        } else {
          b.setXY(x, y, 1 + (int) (Math.random() * 4));
        }
      }
    }
    Set<Shape> allShapes = EnumSet.allOf(Shape.class);
    allShapes.remove(Shape.I1);
    Game g = new Game.Builder()
      .setBoard(b)
      .setSettings(new GameSettings.Builder()
        .player(Turn.FIRST, new Player("", allShapes, 1, true))
        .player(Turn.SECOND, new Player(""))
        .player(Turn.THIRD, new Player(""))
        .player(Turn.FOURTH, new Player(""))
        .build())
      .setTurn(Turn.FIRST)
      .build();
    //long millisBefore = System.currentTimeMillis();
    List<Square> firstSquares = new ArrayList<>();
    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 20; y++) {
        Square s = new Square(x, y);
        if (g.isCorner(s, Turn.FIRST)) {
          firstSquares.add(new Square(x, y));
        }
      }
    }
    //long millisAfter = System.currentTimeMillis();
    System.out.println("corners: " + firstSquares.size());
    //System.out.println(millisAfter - millisBefore);
    System.out.println();

    long millisBefore = System.currentTimeMillis();
    int numMoves = 0;
    for (Shape s : g.getPlayer(Turn.FIRST).getRemainingPieces()) {
      for (Orientation o : Orientation.values()) {
        for (int x = 0; x < 20; x++) {
          for (int y = 0; y < 20; y++) {
            if (g.isLegal(new Move(s, o, x, y))) {
              numMoves++;
            }
          }
        }
      }
    }
    long millisAfter = System.currentTimeMillis();
    System.out.println("numMoves: " + numMoves);
    System.out.println(millisAfter - millisBefore);
  }
}
