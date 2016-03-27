package edu.brown.cs.blokus;

import java.util.Collection;
import java.util.Objects;

/**
 * A square with an x-coordinate and y-coordinate.
 *
 * @author aaronzhang
 */
public class Square {

  /**
   * x-coordinate.
   */
  private final int x;

  /**
   * y-coordinate.
   */
  private final int y;

  /**
   * Square with given x-coordinate and y-coordinate.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   */
  public Square(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Draws squares on a grid with side length 2 * radius + 1. (0, 0) will be in
   * the middle of the grid.
   *
   * @param squares squares to draw
   * @param radius radius of grid, must be positive
   * @return grid representation
   */
  public static String grid(Collection<Square> squares, int radius) {
    // Fill in squares
    boolean[][] grid = new boolean[2 * radius + 1][2 * radius + 1];
    for (Square s : squares) {
      grid[radius - s.getY()][radius + s.getX()] = true;
    }
    // Convert to string
    StringBuilder sb = new StringBuilder();
    for (boolean[] row : grid) {
      for (boolean column : row) {
        sb.append(column ? 'X' : '-');
      }
      sb.append('\n');
    }
    return sb.substring(0, sb.length() - 1);
  }

  /**
   * Translates this square, returning a new square with the coordinates of the
   * result. This square is not mutated.
   *
   * @param deltaX change in x
   * @param deltaY change in y
   * @return new square representing translation
   */
  public Square translate(int deltaX, int deltaY) {
    return new Square(x + deltaX, y + deltaY);
  }

  /**
   * Whether this square has the same coordinates as another square.
   *
   * @param o other object
   * @return equality with other object
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Square)) {
      return false;
    }
    Square other = (Square) o;
    return this.x == other.x && this.y == other.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }

  /**
   * @return x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * @return y-coordinate
   */
  public int getY() {
    return y;
  }
}
