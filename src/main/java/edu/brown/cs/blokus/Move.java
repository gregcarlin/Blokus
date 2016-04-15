package edu.brown.cs.blokus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A move represents placing a piece on the board. Placing a piece on the board
 * requires three pieces of information: the shape, the orientation, and the
 * coordinates of the center of the shape.
 *
 * @author aaronzhang
 */
public class Move {

  /**
   * Shape.
   */
  private final Shape shape;

  /**
   * Orientation.
   */
  private final Orientation orientation;

  /**
   * x-coordiante of center.
   */
  private final int x;

  /**
   * y-coordinate of center.
   */
  private final int y;

  /**
   * Squares covered by the move.
   */
  private final Set<Square> squares;

  /**
   * New move with the given shape, orientation, and coordinates of the center
   * of the shape.
   *
   * @param shape shape
   * @param orientation orientation
   * @param x x-coordinate
   * @param y y-coordinate
   */
  public Move(Shape shape, Orientation orientation, int x, int y) {
    this.shape = shape;
    this.orientation = orientation;
    this.x = x;
    this.y = y;
    // Calculate squares
    this.squares = new HashSet<>();
    shape.getSquares().forEach(
        s -> this.squares.add(orientation.imageOf(s).translate(x, y)));
  }

  /**
   * Returns set of squares covered by this move. The coordinates of the squares
   * are absolute.
   *
   * @return squares covered by this move
   */
  public Set<Square> getSquares() {
    return Collections.unmodifiableSet(squares);
  }

  /**
   * @return shape
   */
  public Shape getShape() {
    return shape;
  }

  /**
   * @return orientation
   */
  public Orientation getOrientation() {
    return orientation;
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
