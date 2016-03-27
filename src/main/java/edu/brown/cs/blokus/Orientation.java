package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.List;

/**
 * The effect of zero or more 90-degree rotations and horizontal or vertical
 * reflections in the plane.
 *
 * @author aaronzhang
 */
public enum Orientation {

  /**
   * Identity.
   */
  E(new Square(1, 0), new Square(0, 1)),
  /**
   * 90-degree rotation.
   */
  S(new Square(0, 1), new Square(-1, 0)),
  /**
   * 180-degree rotation.
   */
  SS(S, S),
  /**
   * 270-degree rotation.
   */
  SSS(SS, S),
  /**
   * Vertical reflection.
   */
  R(new Square(-1, 0), new Square(0, 1)),
  /**
   * Vertical reflection then 90-degree rotation.
   */
  RS(R, S),
  /**
   * Vertical reflection then 180-degree rotation.
   */
  RSS(R, SS),
  /**
   * Vertical reflection then 270-degree rotation.
   */
  RSSS(R, SSS);

  /**
   * Image of the square (1, 0).
   */
  private final int[] x;

  /**
   * Image of the square (0, 1).
   */
  private final int[] y;

  /**
   * New orientation given the images of the squares (1, 0) and (0, 1).
   *
   * @param xImage image of (1, 0)
   * @param yImage image of (0, 1)
   */
  private Orientation(Square xImage, Square yImage) {
    this.x = new int[]{xImage.getX(), xImage.getY()};
    this.y = new int[]{yImage.getX(), yImage.getY()};
  }

  /**
   * New orientation that is the composition of the two orientations.
   *
   * @param first first orientation
   * @param second second orientation
   */
  private Orientation(Orientation first, Orientation second) {
    this(second.imageOf(first.imageOf(new Square(1, 0))),
        second.imageOf(first.imageOf(new Square(0, 1))));
  }

  /**
   * Returns image of square under this orientation.
   *
   * @param input square
   * @return image of square
   */
  public Square imageOf(Square input) {
    int imageX = input.getX() * x[0] + input.getY() * y[0];
    int imageY = input.getX() * x[1] + input.getY() * y[1];
    return new Square(imageX, imageY);
  }

  /**
   * Prints orientations of the F5. Allows visual verification that the
   * orientations are defined correctly.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    final int maxRadius = 2;
    for (Orientation o : values()) {
      System.out.println(o.toString());
      List<Square> squares = new ArrayList<>();
      Shape.F5.getSquares().forEach(s -> squares.add(o.imageOf(s)));
      System.out.println(Square.grid(squares, maxRadius));
      System.out.println();
    }
  }
}
