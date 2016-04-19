package edu.brown.cs.blokus;

import java.util.Arrays;

/**
 * A square board: a grid of squares. Each square may be empty, or it may be
 * occupied by a player.
 */
public class Board {

  /**
   * Default board size.
   */
  public static final int DEFAULT_SIZE = 20;

  /**
   * Grid of squares. An empty square has the value 0; an occupied square has
   * the number of the player that occupies it. Each inner array is a row.
   */
  private final int[][] grid;

  /**
   * New board with given size: the number of squares on each side.
   *
   * @param size size
   */
  public Board(int size) {
    this.grid = new int[size][size];
  }

  /**
   * New board with the given grid of squares. The grid should be a square grid
   * where each inner array represents a row. Modifications to the argument
   * array after construction will not affect this object.
   *
   * @param grid grid of squares
   */
  public Board(int[][] grid) {
    this.grid = copyOf(grid);
  }

  /**
   * New board with the default size {@value Board#DEFAULT_SIZE}.
   */
  public Board() {
    this(DEFAULT_SIZE);
  }

  /**
   * Copies input 2D-array.
   *
   * @param input 2D-array
   * @return copy of input
   */
  private static int[][] copyOf(int[][] input) {
    int[][] copy = new int[input.length][];
    for (int i = 0; i < copy.length; i++) {
      copy[i] = Arrays.copyOf(input[i], input[i].length);
    }
    return copy;
  }

  /**
   * Gets the value at the given row and column.
   *
   * @param row row
   * @param column column
   * @return value
   */
  public int getRowColumn(int row, int column) {
    return grid[row][column];
  }

  /**
   * Sets the value at the given row and column. Returns this board.
   *
   * @param row row
   * @param column column
   * @param value value
   * @return this board
   */
  public Board setRowColumn(int row, int column, int value) {
    grid[row][column] = value;
    return this;
  }

  /**
   * Gets the value at the given x and y-coordinates, with (0, 0) the
   * bottom-left of the grid.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return value
   */
  public int getXY(int x, int y) {
    int[] rc = convertXY(x, y);
    return getRowColumn(rc[0], rc[1]);
  }

  /**
   * Same as {@link Board#getXY(int, int)}, with the x and y-coordinates of the
   * given square.
   *
   * @param square square
   * @return value at square
   */
  public int getSquare(Square square) {
    return getXY(square.getX(), square.getY());
  }

  /**
   * Sets the value at the given x and y-coordinates, with (0, 0) the
   * bottom-left of the grid. Returns this board.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @param value value
   * @return this board
   */
  public Board setXY(int x, int y, int value) {
    int[] rc = convertXY(x, y);
    return setRowColumn(rc[0], rc[1], value);
  }

  /**
   * Converts x and y-coordinates to row and column. x and y-coordinates are
   * interpreted with the bottom-left corner of the grid as the origin.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return row and column
   */
  private int[] convertXY(int x, int y) {
    int row = size() - 1 - y;
    int column = x;
    return new int[]{row, column};
  }

  /**
   * Returns number of squares on a side of the grid.
   *
   * @return size
   */
  public int size() {
    return grid.length;
  }

  /**
   * @return a copy of the grid, where each inner array is a row
   */
  public int[][] getGrid() {
    return copyOf(grid);
  }
  
  /**
   * Gets a copy of the grid indexed by x and y-coordinates.
   * 
   * @return grid indexed by x and y-coordinates
   */
  public int[][] getGridXY() {
    int[][] gridXY = new int[grid.length][grid.length];
    for (int x = 0; x < gridXY.length; x++) {
      for (int y = 0; y < gridXY.length; y++) {
        gridXY[x][y] = getXY(x, y);
      }
    }
    return gridXY;
  }

  /**
   * Makes a move on the board with the specified mark and returns this board.
   * The mark is the number of the player (1, 2, 3, or 4). This method may be
   * useful in testing to construct a board to use for Game tests.
   *
   * @param move move
   * @param mark mark
   * @return this board
   */
  public Board makeMove(Move move, int mark) {
    for (Square moveSquare : move.getSquares()) {
      setXY(moveSquare.getX(), moveSquare.getY(), mark);
    }
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int[] row : grid) {
      for (int column : row) {
        sb.append(column);
      }
      sb.append('\n');
    }
    String contents = size() == 0 ? "" : sb.substring(0, sb.length() - 1);
    return String.format("[Board:%n%s%n]", contents);
  }
}
