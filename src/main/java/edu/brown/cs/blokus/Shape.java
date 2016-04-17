package edu.brown.cs.blokus;

import static edu.brown.cs.blokus.Shape.Symmetry.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A shape covers a set of squares.
 *
 * @author aaronzhang
 */
public enum Shape {

  /**
   * X
   */
  I1(ALL,
    0, 0),
  
  /**
   * XO
   */
  I2(POINT_LINE,
    0, 0,
    1, 0),
  
  /**
   * OXO
   */
  I3(POINT_LINE,
    0, 0,
    -1, 0,
    1, 0),
  
  /**
   * O
   * XO
   */
  V3(LINE,
    0, 0,
    0, 1,
    1, 0),
  
  /**
   * OXOO
   */
  I4(POINT_LINE,
    0, 0,
    -1, 0,
    1, 0,
    2, 0),
  
  /**
   * O
   * OXO
   */
  L4(NONE,
    0, 0,
    -1, 0,
    -1, 1,
    1, 0),
  
  /**
   * OO
   * XO
   */
  O4(ALL,
    0, 0,
    0, 1,
    1, 0,
    1, 1),
  
  /**
   *  O
   * OXO
   */
  T4(LINE,
    0, 0,
    -1, 0,
    0, 1,
    1, 0),
  
  /**
   *  OO
   * OX
   */
  Z4(POINT,
    0, 0,
    -1, 0,
    0, 1,
    1, 1),
  
  /**
   *  O
   *  XO
   * OO
   */
  F5(NONE,
    0, 0,
    -1, -1,
    0, -1,
    0, 1,
    1, 0),
  
  /**
   * OOXOO
   */
  I5(POINT_LINE,
    0, 0,
    -2, 0,
    -1, 0,
    1, 0,
    2, 0),
  
  /**
   *    O
   * OOXO
   */
  L5(NONE,
    0, 0,
    -2, 0,
    -1, 0,
    1, 0,
    1, 1),
  
  /**
   *   OO
   * OOX
   */
  N5(NONE,
    0, 0,
    -2, 0,
    -1, 0,
    0, 1,
    1, 1),
  
  /**
   * OO
   * XO
   * O
   */
  P5(NONE,
    0, 0,
    0, -1,
    0, 1,
    1, 0,
    1, 1),
  
  /**
   * O
   * OXO
   * O
   */
  T5(LINE,
    0, 0,
    -1, -1,
    -1, 0,
    -1, 1,
    1, 0),
  
  /**
   * OO
   * X
   * OO
   */
  U5(LINE,
    0, 0,
    0, -1,
    0, 1,
    1, -1,
    1, 1),
  
  /**
   * O
   * O
   * XOO
   */
  V5(LINE,
    0, 0,
    0, 1,
    0, 2,
    1, 0,
    2, 0),
  
  /**
   * O
   * OX
   *  OO
   */
  W5(LINE,
    0, 0,
    -1, 1,
    -1, 0,
    0, -1,
    1, -1),
  
  /**
   *  O
   * OXO
   *  O
   */
  X5(ALL,
    0, 0,
    -1, 0,
    0, -1,
    0, 1,
    1, 0),
  
  /**
   *   O
   * OOXO
   */
  Y5(NONE,
    0, 0,
    -2, 0,
    -1, 0,
    0, 1,
    1, 0),
  
  /**
   *  OO
   *  X
   * OO
   */
  Z5(POINT,
    0, 0,
    -1, -1,
    0, -1,
    0, 1,
    1, 1);
  
  /**
   * Symmetries of a shape. Symmetries describe orientations that make the shape
   * look the same. Two orientations make the shape "look the same" if the
   * pattern of X's in the grid representation of the shape under the two
   * orientations is the same, up to translation. For example, the grid
   * representation of O4 under E does not equal the grid representation of O4
   * under S, but they are the same modulo translation.
   */
  enum Symmetry {

    /**
     * No symmetries. All eight orientations look different.
     */
    NONE,
    /**
     * Symmetry under point reflection. SS looks the same as E.
     */
    POINT,
    /**
     * Symmetry under line reflection. R looks the same as E.
     */
    LINE,
    /**
     * Symmetry under point reflection and line reflection. All orientations
     * look the same as one of {E, S}.
     */
    POINT_LINE,
    /**
     * All eight orientations look the same.
     */
    ALL;

    /**
     * Distinct orientations for each symmetry.
     */
    private static final EnumMap<Symmetry, Set<Orientation>> DISTINCT_ORIENTATIONS;

    /**
     * Initializes distinct orientations map.
     */
    static {
      DISTINCT_ORIENTATIONS = new EnumMap<>(Symmetry.class);
      DISTINCT_ORIENTATIONS.put(NONE, Collections.unmodifiableSet(
        EnumSet.allOf(Orientation.class)));
      DISTINCT_ORIENTATIONS.put(POINT, Collections.unmodifiableSet(EnumSet.of(
        Orientation.E,
        Orientation.S,
        Orientation.R,
        Orientation.RS)));
      DISTINCT_ORIENTATIONS.put(LINE, Collections.unmodifiableSet(EnumSet.of(
        Orientation.E,
        Orientation.S,
        Orientation.SS,
        Orientation.SSS)));
      DISTINCT_ORIENTATIONS.put(POINT_LINE,
        Collections.unmodifiableSet(EnumSet.of(
            Orientation.E,
            Orientation.S)));
      DISTINCT_ORIENTATIONS.put(ALL, Collections.unmodifiableSet(
        EnumSet.of(Orientation.E)));
    }
  }

  /**
   * Symmetries.
   */
  private final Symmetry symmetries;

  /**
   * Squares that this shape covers.
   */
  private final Set<Square> squares;

  /**
   * Number of shapes.
   */
  public static final int NUM_SHAPES = 21;
  
  /**
   * Maximum radius of a shape.
   */
  public static final int MAX_RADIUS = 2;

  /**
   * New shape with symmetries and given squares, where each pair of integers
   * represents the x-coordinate and y-coordinate of a square.
   *
   * @param coordinates coordinates of squares
   */
  private Shape(Symmetry symmetries, int... coordinates) {
    this.symmetries = symmetries;
    this.squares = new HashSet<>();
    for (int i = 0; i < coordinates.length; i += 2) {
      this.squares.add(new Square(coordinates[i], coordinates[i + 1]));
    }
  }

  /**
   * Returns set of squares that this shape covers. One square (not necessarily
   * covered by the shape) is designated the center, represented by the square
   * (0, 0). The coordinates of all other squares are relative to the center, so
   * the square directly to the left of the center would be (-1, 0), for
   * example.
   *
   * @return set of squares in this shape
   */
  public Set<Square> getSquares() {
    return Collections.unmodifiableSet(squares);
  }

  /**
   * @return number of squares in this shape
   */
  public int size() {
    return squares.size();
  }

  /**
   * Whether this shape contains the square.
   *
   * @param square square
   * @return whether this shape contains the square
   */
  public boolean contains(Square square) {
    return squares.contains(square);
  }

  /**
   * Returns set of distinct orientations of this shape. Two orientations are
   * considered non-distinct if the images of this shape under the two
   * orientations are the same, up to translation.
   *
   * @return distinct orientations
   */
  public Set<Orientation> distinctOrientations() {
    return Symmetry.DISTINCT_ORIENTATIONS.get(symmetries);
  }

  /**
   * Prints each shape on a grid. Allows visual verification that the shapes
   * have the right squares.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    final int maxRadius = 2;
    for (Shape shape : values()) {
      System.out.println(shape.toString());
      System.out.println(Square.grid(shape.getSquares(), maxRadius));
      System.out.println();
    }
  }
}
