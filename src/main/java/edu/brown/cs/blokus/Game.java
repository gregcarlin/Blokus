package edu.brown.cs.blokus;

import edu.brown.cs.blokus.ai.AI;
import edu.brown.cs.blokus.ai.RandomAI;
import edu.brown.cs.blokus.ai.TotalComponentSizeAI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import edu.brown.cs.blokus.handlers.LiveUpdater;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

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
     * {@value Board#DEFAULT_SIZE}.
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
     * {@value Board#DEFAULT_SIZE}.
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
     * Sets game settings. This method must be called.
     *
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
      if (game.settings.getState() == GameSettings.State.PLAYING) {
        game.checkTime();
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
   * Checks the current time and makes random moves for players who should be
   * skipped because they ran out of time.
   */
  public void checkTime() {
    final int timer = settings.getTimer();
    if (timer == 0) {
      return;
    }

    long timerMillis = 1000 * timer;
    long automaticMoveTime = settings.getLastTurnTime() + timerMillis;
    long currentTime = System.currentTimeMillis();
    while (automaticMoveTime < currentTime && turn != null) {
      makeMove(getRandomMove(turn));
      settings.setLastTurnTime(automaticMoveTime);
      automaticMoveTime += timerMillis;
    }
  }

  /**
   * Returns the remaining time for the turn player, in milliseconds.
   *
   * @return remaining time in milliseconds
   */
  public long remainingTimeMillis() {
    if (settings.getTimer() == 0) {
      return Long.MAX_VALUE;
    }
    checkTime();
    return System.currentTimeMillis() - settings.getLastTurnTime();
  }

  /**
   * Whether the move is legal for the player with the given turn. For a move to
   * be legal, the turn player must have the piece being played. All the squares
   * that the move covers must be unoccupied squares on the board. No square in
   * the move can share an edge with a square already on the board with the same
   * color. At least one square in the move must share a corner with a square
   * already on the board with the same color.
   *
   * @param move move
   * @param turn turn
   * @return whether the move is legal
   */
  public boolean isLegal(Move move, Turn turn) {
    // Player must have the piece being played
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
    if (firstMove(turn)) {
      Square corner = getCorner(turn);
      return move.getSquares().contains(corner);
    }

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
   * Whether the move is legal for the turn player.
   *
   * @param move move
   * @return whether the move is legal
   */
  public boolean isLegal(Move move) {
    return isLegal(move, turn);
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
   * Makes a move and advances to the next turn.
   *
   * @param move move
   */
  public void makeMove(Move move) {
    makeMove(move, System.currentTimeMillis());
  }

  /**
   * Makes a move with the given timestamp.
   *
   * @param move move
   * @param timestamp timestamp
   */
  public void makeMove(Move move, long timestamp) {
    board.makeMove(move, turn.mark());
    getPlayer(turn).usePiece(move.getShape());
    settings.setLastTurnTime(timestamp);
    LiveUpdater.moveMade(this, move);
    turn = nextPlaying();
    if (turn == null) {
      settings.setState(GameSettings.State.FINISHED);
    }
  }

  /**
   * Tries a move and applies the function to the game after the move is made.
   * When trying a move, the board and players are updated, but not game
   * settings. The game is reverted to its original state after the function
   * returns.
   *
   * @param <T> function return type
   * @param move move to try
   * @param f function on game state after move
   * @return result of function
   */
  public <T> T tryMove(Move move, Function<? super Game, T> f) {
    board.makeMove(move, turn.mark());
    getPlayer(turn).usePiece(move.getShape());
    T result = f.apply(this);
    undoTryMove(move);
    return result;
  }

  /**
   * Returns the game to its original state after trying a move.
   *
   * @param move move to undo
   */
  private void undoTryMove(Move move) {
    for (Square square : move.getSquares()) {
      board.setXY(square.getX(), square.getY(), 0);
    }
    getPlayer(turn).undoUsePiece(move.getShape());
  }

  /**
   * Whether the player can move. If not, the player's
   * {@link Player#stopPlaying()} method will be called.
   *
   * @param turn the turn of the player
   * @return true if the player can move, false otherwise
   */
  public boolean canMove(Turn turn) {
    if (!getPlayer(turn).isPlaying()) {
      return false;
    }
    for (Shape shape : getPlayer(turn).getRemainingPieces()) {
      for (Orientation o : shape.distinctOrientations()) {
        for (int x = 0, s = board.size(); x < s; x++) {
          for (int y = 0; y < s; y++) {
            Move move = new Move(shape, o, x, y);
            if (isLegal(move, turn)) {
              return true;
            }
          }
        }
      }
    }
    getPlayer(turn).stopPlaying();
    return false;
  }

  /**
   * Returns a random move for the player with the given turn, or null if the
   * player has no legal moves.
   *
   * @param turn turn
   * @return random move, if any
   */
  public Move getRandomMove(Turn turn) {
    List<Move> legalMoves = getLegalMoves(turn);
    if (legalMoves.isEmpty()) {
      getPlayer(turn).stopPlaying();
      return null;
    }
    return legalMoves.get((int) (Math.random() * legalMoves.size()));
  }

  /**
   * Gets legal moves for the player with the given turn. No two moves in the
   * returned list occupy exactly the same set of squares. The list of sorted by
   * number of squares occupied by the move, in ascending order. Using a list
   * instead of a set also allows quick choice of a random move.
   *
   * @param turn turn
   * @return legal moves
   */
  public List<Move> getLegalMoves(Turn turn) {
    if (!getPlayer(turn).isPlaying()) {
      return Collections.emptyList();
    }
    List<Move> legalMoves = new ArrayList<>();
    for (Shape shape : getPlayer(turn).getRemainingPieces()) {
      for (Orientation o : shape.distinctOrientations()) {
        for (int x = 0, s = board.size(); x < s; x++) {
          for (int y = 0; y < s; y++) {
            Move move = new Move(shape, o, x, y);
            if (isLegal(move, turn)) {
              legalMoves.add(move);
            }
          }
        }
      }
    }
    return legalMoves;
  }

  /**
   * Returns whether the game is over. If so, updates the game state in the game
   * settings.
   *
   * @return whether game is over
   */
  public boolean isGameOver() {
    switch (getSettings().getState()) {
      case FINISHED:
        return true;
      case UNSTARTED:
        return false;
      case PLAYING:
        boolean gameOver = nextPlaying() == null;
        if (gameOver) {
          getSettings().setState(GameSettings.State.FINISHED);
        }
        return gameOver;
      default:
        throw new IllegalStateException("wut");
    }
  }

  /**
   * Returns the turn of the next player that is still playing, or null if no
   * more players are playing.
   *
   * @return next turn, if any
   */
  public Turn nextPlaying() {
    Turn next = turn.next();
    do {
      if (getPlayer(next).isPlaying() && canMove(next)) {
        return next;
      }
      next = next.next();
    } while (next != turn.next());
    return null;
  }

  /**
   * @return the board
   */
  public Board getBoard() {
    return board;
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
   * Gets the grid of the board, indexed by x and y-coordinates.
   *
   * @return grid indexed by x and y-coordinates
   */
  public int[][] getGridXY() {
    return board.getGridXY();
  }

  /**
   * Gets the grid of the board as a 2d list.
   *
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
   * Gets the settings of this game.
   *
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
   *
   * @return a collection of all players
   */
  public Collection<Player> getAllPlayers() {
    return getSettings().getAllPlayers();
  }

  /**
   * Whether or not a player is a member of this game.
   *
   * @param id the id of the player to check
   * @return true if they're a member, false otherwise
   */
  public boolean hasUser(String id) {
    for (Player p : getAllPlayers()) {
      if (id.equals(p.getId())) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) throws Exception {
    Board b = new Board(20);
    //b.setXY(10, 10, 1);
    //b.setXY(10, 12, 1);

    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 20; y++) {
        b.setXY(x, y, (int) (Math.random() * 5));
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
    //TotalComponentSizeAI ai = new TotalComponentSizeAI();
    long before = System.currentTimeMillis();
    //AI.simulateAndSaveGame(TotalComponentSizeAI::new, RandomAI::new,
      //RandomAI::new, RandomAI::new, "/home/aaronzhang/game1.blksgf");
    long after = System.currentTimeMillis();
    System.out.println("time: " + (after - before));
  }
}
