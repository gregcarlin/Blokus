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
  E(new Square(1, 0), new Square(0, 1)) {
      @Override
      public Orientation compose(Orientation o2) {
        return o2;
      }
    },
  /**
   * 90-degree rotation.
   */
  S(new Square(0, 1), new Square(-1, 0)) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return S;
          case S:
            return SS;
          case SS:
            return SSS;
          case SSS:
            return E;
          case R:
            return RSSS;
          case RS:
            return R;
          case RSS:
            return RS;
          case RSSS:
            return RSS;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * 180-degree rotation.
   */
  SS(S, S) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return SS;
          case S:
            return SSS;
          case SS:
            return E;
          case SSS:
            return S;
          case R:
            return RSS;
          case RS:
            return RSSS;
          case RSS:
            return R;
          case RSSS:
            return RS;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * 270-degree rotation.
   */
  SSS(SS, S) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return SSS;
          case S:
            return E;
          case SS:
            return S;
          case SSS:
            return SS;
          case R:
            return RS;
          case RS:
            return RSS;
          case RSS:
            return RSSS;
          case RSSS:
            return R;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * Reflection across the vertical axis.
   */
  R(new Square(-1, 0), new Square(0, 1)) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return R;
          case S:
            return RS;
          case SS:
            return RSS;
          case SSS:
            return RSSS;
          case R:
            return E;
          case RS:
            return S;
          case RSS:
            return SS;
          case RSSS:
            return SSS;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * Vertical reflection then 90-degree rotation.
   */
  RS(R, S) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return RS;
          case S:
            return RSS;
          case SS:
            return RSSS;
          case SSS:
            return R;
          case R:
            return SSS;
          case RS:
            return E;
          case RSS:
            return S;
          case RSSS:
            return SS;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * Vertical reflection then 180-degree rotation.
   */
  RSS(R, SS) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return RSS;
          case S:
            return RSSS;
          case SS:
            return R;
          case SSS:
            return RS;
          case R:
            return SS;
          case RS:
            return SSS;
          case RSS:
            return E;
          case RSSS:
            return S;
          default:
            throw new AssertionError();
        }
      }
    },
  /**
   * Vertical reflection then 270-degree rotation.
   */
  RSSS(R, SSS) {
      @Override
      public Orientation compose(Orientation o2) {
        switch (o2) {
          case E:
            return RSSS;
          case S:
            return R;
          case SS:
            return RS;
          case SSS:
            return RSS;
          case R:
            return S;
          case RS:
            return SS;
          case RSS:
            return SSS;
          case RSSS:
            return E;
          default:
            throw new AssertionError();
        }
      }
    };

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
   * Returns the orientation obtained by performing the first orientation, then
   * the second orientation.
   *
   * @param first first orientation
   * @param second second orientation
   * @return composition
   */
  public static Orientation compose(Orientation first, Orientation second) {
    Square image1 = second.imageOf(first.imageOf(new Square(1, 0)));
    Square image2 = second.imageOf(first.imageOf(new Square(0, 1)));
    for (Orientation o : values()) {
      if (o.imageOf(new Square(1, 0)).equals(image1)
        && o.imageOf(new Square(0, 1)).equals(image2)) {
        return o;
      }
    }
    throw new AssertionError();
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

  public abstract Orientation compose(Orientation o2);

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
