package edu.brown.cs.blokus.legal;

import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Orientation;
import static edu.brown.cs.blokus.Orientation.*;
import edu.brown.cs.blokus.Shape;
import static edu.brown.cs.blokus.Shape.*;
import static edu.brown.cs.blokus.legal.Corner.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Legal moves for each piece. Reduces the number of moves that have to be
 * checked to get all legal moves.
 *
 * @author aaronzhang
 */
public class LegalMoves {

  // Legal moves by corner type
  private static final Map<Corner, Multimap<Shape, RelativeMove>> relativeMoves
    = new EnumMap<>(Corner.class);
  private static final Multimap<Shape, RelativeMove> bottomLeft = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> topLeft = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> bottomRight = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> topRight = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> left = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> right = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> bottom = HashMultimap.create();
  private static final Multimap<Shape, RelativeMove> top = HashMultimap.create();

  /**
   * A move relative to (0, 0). Does not compute occupied squares.
   */
  private static class RelativeMove {

    private final Shape shape;
    private final Orientation orientation;
    private final int x;
    private final int y;

    private RelativeMove(Shape shape, Orientation orientation, int x, int y) {
      this.shape = shape;
      this.orientation = orientation;
      this.x = x;
      this.y = y;
    }

    private Move translate(int deltaX, int deltaY) {
      return new Move(shape, orientation, x + deltaX, y + deltaY);
    }
  }

  /**
   * Ensures that the maps of legal moves will be loaded. The maps of legal
   * moves will be loaded the first time a method of this class is called;
   * calling this method loads the moves right away instead of taking more time
   * to load moves during, for example, the first request from the frontend.
   */
  public static void load() {

  }

  /**
   * Fills multimaps of moves.
   */
  static {
    bottomLeft();
    transform(bottomLeft, topLeft, RSS);
    transform(bottomLeft, bottomRight, R);
    transform(bottomLeft, topRight, SS);
    left();
    transform(left, right, R);
    transform(left, bottom, S);
    transform(left, top, SSS);
    relativeMoves.put(BOTTOM_LEFT, bottomLeft);
    relativeMoves.put(TOP_LEFT, topLeft);
    relativeMoves.put(BOTTOM_RIGHT, bottomRight);
    relativeMoves.put(TOP_RIGHT, topRight);
    relativeMoves.put(LEFT, left);
    relativeMoves.put(RIGHT, right);
    relativeMoves.put(BOTTOM, bottom);
    relativeMoves.put(TOP, top);
  }

  /**
   * Bottom-left corner.
   */
  private static void bottomLeft() {
    bottomLeft.put(I2, new RelativeMove(I2, E, 0, 0));
    bottomLeft.put(I2, new RelativeMove(I2, S, 0, 0));
    bottomLeft.put(I3, new RelativeMove(I3, E, 1, 0));
    bottomLeft.put(I3, new RelativeMove(I3, S, 0, 1));
    bottomLeft.put(V3, new RelativeMove(V3, E, 0, 0));
    bottomLeft.put(V3, new RelativeMove(V3, S, 1, 0));
    bottomLeft.put(V3, new RelativeMove(V3, SS, 0, 1));
    bottomLeft.put(V3, new RelativeMove(V3, SS, 1, 0));
    bottomLeft.put(V3, new RelativeMove(V3, SSS, 0, 1));
    bottomLeft.put(I4, new RelativeMove(I4, E, 1, 0));
    bottomLeft.put(I4, new RelativeMove(I4, S, 0, 1));
    bottomLeft.put(L4, new RelativeMove(L4, E, 1, 0));
    bottomLeft.put(L4, new RelativeMove(L4, S, 1, 1));
    bottomLeft.put(L4, new RelativeMove(L4, SS, -1, 1));
    bottomLeft.put(L4, new RelativeMove(L4, SS, 1, 0));
    bottomLeft.put(L4, new RelativeMove(L4, SSS, 0, 1));
    bottomLeft.put(L4, new RelativeMove(L4, R, 1, 0));
    bottomLeft.put(L4, new RelativeMove(L4, RS, 0, 1));
    bottomLeft.put(L4, new RelativeMove(L4, RS, 1, -1));
    bottomLeft.put(L4, new RelativeMove(L4, RSS, 1, 1));
    bottomLeft.put(L4, new RelativeMove(L4, RSSS, 0, 1));
    bottomLeft.put(O4, new RelativeMove(O4, E, 0, 0));
    bottomLeft.put(T4, new RelativeMove(T4, E, 1, 0));
    bottomLeft.put(T4, new RelativeMove(T4, S, 0, 1));
    bottomLeft.put(T4, new RelativeMove(T4, S, 1, 0));
    bottomLeft.put(T4, new RelativeMove(T4, SS, 0, 1));
    bottomLeft.put(T4, new RelativeMove(T4, SS, 1, 0));
    bottomLeft.put(T4, new RelativeMove(T4, SSS, 0, 1));
    bottomLeft.put(Z4, new RelativeMove(Z4, E, 1, 0));
    bottomLeft.put(Z4, new RelativeMove(Z4, S, 0, 1));
    bottomLeft.put(Z4, new RelativeMove(Z4, S, 1, 0));
    bottomLeft.put(Z4, new RelativeMove(Z4, R, 0, 0));
    bottomLeft.put(Z4, new RelativeMove(Z4, R, 1, -1));
    bottomLeft.put(Z4, new RelativeMove(Z4, RS, 1, 1));
    bottomLeft.put(F5, new RelativeMove(F5, E, 1, 1));
    bottomLeft.put(F5, new RelativeMove(F5, S, -1, 1));
    bottomLeft.put(F5, new RelativeMove(F5, S, 1, 0));
    bottomLeft.put(F5, new RelativeMove(F5, SS, 0, 1));
    bottomLeft.put(F5, new RelativeMove(F5, SS, 1, 0));
    bottomLeft.put(F5, new RelativeMove(F5, SSS, 0, 1));
    bottomLeft.put(F5, new RelativeMove(F5, SSS, 1, 0));
    bottomLeft.put(F5, new RelativeMove(F5, R, 0, 1));
    bottomLeft.put(F5, new RelativeMove(F5, R, 1, 0));
    bottomLeft.put(F5, new RelativeMove(F5, RS, 0, 1));
    bottomLeft.put(F5, new RelativeMove(F5, RS, 1, 0));
    bottomLeft.put(F5, new RelativeMove(F5, RSS, 0, 1));
    bottomLeft.put(F5, new RelativeMove(F5, RSS, 1, -1));
    bottomLeft.put(F5, new RelativeMove(F5, RSSS, 1, 1));
    bottomLeft.put(I5, new RelativeMove(I5, E, 2, 0));
    bottomLeft.put(I5, new RelativeMove(I5, S, 0, 2));
    bottomLeft.put(L5, new RelativeMove(L5, E, 2, 0));
    bottomLeft.put(L5, new RelativeMove(L5, S, 0, 2));
    bottomLeft.put(L5, new RelativeMove(L5, S, 1, -1));
    bottomLeft.put(L5, new RelativeMove(L5, SS, 1, 1));
    bottomLeft.put(L5, new RelativeMove(L5, SSS, 0, 1));
    bottomLeft.put(L5, new RelativeMove(L5, R, 1, 0));
    bottomLeft.put(L5, new RelativeMove(L5, RS, 1, 1));
    bottomLeft.put(L5, new RelativeMove(L5, RSS, -1, 1));
    bottomLeft.put(L5, new RelativeMove(L5, RSS, 2, 0));
    bottomLeft.put(L5, new RelativeMove(L5, RSSS, 0, 2));
    bottomLeft.put(N5, new RelativeMove(N5, E, 2, 0));
    bottomLeft.put(N5, new RelativeMove(N5, S, 0, 2));
    bottomLeft.put(N5, new RelativeMove(N5, S, 1, 0));
    bottomLeft.put(N5, new RelativeMove(N5, SS, 1, 1));
    bottomLeft.put(N5, new RelativeMove(N5, SSS, -1, 1));
    bottomLeft.put(N5, new RelativeMove(N5, SSS, 0, 0));
    bottomLeft.put(N5, new RelativeMove(N5, R, 0, 0));
    bottomLeft.put(N5, new RelativeMove(N5, R, 1, -1));
    bottomLeft.put(N5, new RelativeMove(N5, RS, 1, 1));
    bottomLeft.put(N5, new RelativeMove(N5, RSS, 0, 1));
    bottomLeft.put(N5, new RelativeMove(N5, RSS, 2, 0));
    bottomLeft.put(N5, new RelativeMove(N5, RSSS, 0, 2));
    bottomLeft.put(P5, new RelativeMove(P5, E, 0, 1));
    bottomLeft.put(P5, new RelativeMove(P5, S, 1, 0));
    bottomLeft.put(P5, new RelativeMove(P5, SS, 1, 1));
    bottomLeft.put(P5, new RelativeMove(P5, SSS, 0, 1));
    bottomLeft.put(P5, new RelativeMove(P5, SSS, 1, 0));
    bottomLeft.put(P5, new RelativeMove(P5, R, 0, 1));
    bottomLeft.put(P5, new RelativeMove(P5, R, 1, 0));
    bottomLeft.put(P5, new RelativeMove(P5, RS, 1, 1));
    bottomLeft.put(P5, new RelativeMove(P5, RSS, 0, 1));
    bottomLeft.put(P5, new RelativeMove(P5, RSSS, 1, 0));
    bottomLeft.put(T5, new RelativeMove(T5, E, 1, 1));
    bottomLeft.put(T5, new RelativeMove(T5, S, 1, 1));
    bottomLeft.put(T5, new RelativeMove(T5, SS, -1, 1));
    bottomLeft.put(T5, new RelativeMove(T5, SS, 1, 0));
    bottomLeft.put(T5, new RelativeMove(T5, SSS, 0, 1));
    bottomLeft.put(T5, new RelativeMove(T5, SSS, 1, -1));
    bottomLeft.put(U5, new RelativeMove(U5, E, 0, 1));
    bottomLeft.put(U5, new RelativeMove(U5, S, 1, 0));
    bottomLeft.put(U5, new RelativeMove(U5, SS, 1, -1));
    bottomLeft.put(U5, new RelativeMove(U5, SS, 1, 1));
    bottomLeft.put(U5, new RelativeMove(U5, SSS, -1, 1));
    bottomLeft.put(U5, new RelativeMove(U5, SSS, 1, 1));
    bottomLeft.put(V5, new RelativeMove(V5, E, 0, 0));
    bottomLeft.put(V5, new RelativeMove(V5, S, 2, 0));
    bottomLeft.put(V5, new RelativeMove(V5, SS, 0, 2));
    bottomLeft.put(V5, new RelativeMove(V5, SS, 2, 0));
    bottomLeft.put(V5, new RelativeMove(V5, SSS, 0, 2));
    bottomLeft.put(W5, new RelativeMove(W5, E, 0, 1));
    bottomLeft.put(W5, new RelativeMove(W5, E, 1, 0));
    bottomLeft.put(W5, new RelativeMove(W5, S, 1, 1));
    bottomLeft.put(W5, new RelativeMove(W5, SS, -1, 1));
    bottomLeft.put(W5, new RelativeMove(W5, SS, 0, 0));
    bottomLeft.put(W5, new RelativeMove(W5, SS, 1, -1));
    bottomLeft.put(W5, new RelativeMove(W5, SSS, 1, 1));
    bottomLeft.put(X5, new RelativeMove(X5, E, 0, 1));
    bottomLeft.put(X5, new RelativeMove(X5, E, 1, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, E, 2, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, S, 0, 2));
    bottomLeft.put(Y5, new RelativeMove(Y5, S, 1, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, SS, 0, 1));
    bottomLeft.put(Y5, new RelativeMove(Y5, SS, 1, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, SSS, 0, 1));
    bottomLeft.put(Y5, new RelativeMove(Y5, R, 1, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, RS, 0, 1));
    bottomLeft.put(Y5, new RelativeMove(Y5, RS, 1, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, RSS, 0, 1));
    bottomLeft.put(Y5, new RelativeMove(Y5, RSS, 2, 0));
    bottomLeft.put(Y5, new RelativeMove(Y5, RSSS, 0, 2));
    bottomLeft.put(Z5, new RelativeMove(Z5, E, 1, 1));
    bottomLeft.put(Z5, new RelativeMove(Z5, S, -1, 1));
    bottomLeft.put(Z5, new RelativeMove(Z5, S, 1, 0));
    bottomLeft.put(Z5, new RelativeMove(Z5, R, 0, 1));
    bottomLeft.put(Z5, new RelativeMove(Z5, R, 1, -1));
    bottomLeft.put(Z5, new RelativeMove(Z5, RS, 1, 1));
  }

  /**
   * Left corner.
   */
  private static void left() {
    left.put(I2, new RelativeMove(I2, E, 0, 0));
    left.put(I3, new RelativeMove(I3, E, 1, 0));
    left.put(V3, new RelativeMove(V3, S, 1, 0));
    left.put(V3, new RelativeMove(V3, SS, 1, 0));
    left.put(I4, new RelativeMove(I4, E, 1, 0));
    left.put(L4, new RelativeMove(L4, S, 1, 1));
    left.put(L4, new RelativeMove(L4, SS, 1, 0));
    left.put(L4, new RelativeMove(L4, R, 1, 0));
    left.put(L4, new RelativeMove(L4, RS, 1, -1));
    left.put(T4, new RelativeMove(T4, E, 1, 0));
    left.put(T4, new RelativeMove(T4, S, 1, 0));
    left.put(T4, new RelativeMove(T4, SS, 1, 0));
    left.put(Z4, new RelativeMove(Z4, E, 1, 0));
    left.put(Z4, new RelativeMove(Z4, R, 1, -1));
    left.put(F5, new RelativeMove(F5, E, 1, 1));
    left.put(F5, new RelativeMove(F5, S, 1, 0));
    left.put(F5, new RelativeMove(F5, SS, 1, 0));
    left.put(F5, new RelativeMove(F5, R, 1, 0));
    left.put(F5, new RelativeMove(F5, RS, 1, 0));
    left.put(F5, new RelativeMove(F5, RSS, 1, -1));
    left.put(I5, new RelativeMove(I5, E, 2, 0));
    left.put(L5, new RelativeMove(L5, E, 2, 0));
    left.put(L5, new RelativeMove(L5, S, 1, -1));
    left.put(L5, new RelativeMove(L5, RS, 1, 1));
    left.put(L5, new RelativeMove(L5, RSS, 2, 0));
    left.put(N5, new RelativeMove(N5, E, 2, 0));
    left.put(N5, new RelativeMove(N5, SS, 1, 1));
    left.put(N5, new RelativeMove(N5, R, 1, -1));
    left.put(N5, new RelativeMove(N5, RSS, 2, 0));
    left.put(P5, new RelativeMove(P5, SSS, 1, 0));
    left.put(P5, new RelativeMove(P5, RSSS, 1, 0));
    left.put(T5, new RelativeMove(T5, S, 1, 1));
    left.put(T5, new RelativeMove(T5, SS, 1, 0));
    left.put(T5, new RelativeMove(T5, SSS, 1, -1));
    left.put(U5, new RelativeMove(U5, SS, 1, -1));
    left.put(U5, new RelativeMove(U5, SS, 1, 1));
    left.put(V5, new RelativeMove(V5, S, 2, 0));
    left.put(V5, new RelativeMove(V5, SS, 2, 0));
    left.put(W5, new RelativeMove(W5, S, 1, 1));
    left.put(W5, new RelativeMove(W5, SS, 1, -1));
    left.put(X5, new RelativeMove(X5, E, 1, 0));
    left.put(Y5, new RelativeMove(Y5, E, 2, 0));
    left.put(Y5, new RelativeMove(Y5, S, 1, 0));
    left.put(Y5, new RelativeMove(Y5, SS, 1, 0));
    left.put(Y5, new RelativeMove(Y5, R, 1, 0));
    left.put(Y5, new RelativeMove(Y5, RS, 1, 0));
    left.put(Y5, new RelativeMove(Y5, RSS, 2, 0));
    left.put(Z5, new RelativeMove(Z5, E, 1, 1));
    left.put(Z5, new RelativeMove(Z5, R, 1, -1));
  }

  /**
   * Transforms each relative move in the source multimap and puts it in the
   * target multimap.
   *
   * @param target target multimap
   * @param transform transformation
   */
  private static void transform(Multimap<Shape, RelativeMove> source,
    Multimap<Shape, RelativeMove> target, Orientation transform) {
    for (Shape shape : source.keySet()) {
      for (RelativeMove r : source.get(shape)) {
        Square image = transform.imageOf(new Square(r.x, r.y));
        target.put(shape, new RelativeMove(shape,
          r.orientation.compose(transform), image.getX(), image.getY()));
      }
    }
  }

  /**
   * Whether the move covers only available squares.
   *
   * @param m move
   * @param available available
   * @return whether move is valid
   */
  private static boolean isAvailable(Move m, Available a) {
    if (!a.available(m.getX(), m.getY())) {
      return false;
    }
    for (Square s : m.getSquares()) {
      if (!a.available(s)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets legal moves for the player with the given turn.
   *
   * @param g game
   * @param turn turn
   * @return legal moves
   */
  public static List<Move> legalMoves(Game g, Turn turn) {
    if (!g.getPlayer(turn).isPlaying()) {
      return Collections.emptyList();
    }
    List<Move> moves = new ArrayList<>();
    Available a = new Available(g, turn);
    Map<Square, Corner> corners = a.cornerTypes();
    Map<Square, Integer> cornerSizes = a.cornerSizes(5);
    Set<Shape> pieces = g.getPlayer(turn).getRemainingPieces();
    for (Shape shape : pieces) {
      for (Square s : a.corners()) {
        if (shape == I1) {
          moves.add(new Move(I1, E, s.getX(), s.getY()));
        } else {
          if (cornerSizes.get(s) >= shape.size()) {
            for (RelativeMove r : relativeMoves.get(corners.get(s)).get(shape)) {
              Move m = r.translate(s.getX(), s.getY());
              if (isAvailable(m, a)) {
                moves.add(m);
              }
            }
          }
        }
      }
    }
    return moves;
  }

  /**
   * Whether the player with the given turn has legal moves.
   *
   * @param g game
   * @param turn turn
   * @return whether player has legal moves
   */
  public static boolean canMove(Game g, Turn turn) {
    if (!g.getPlayer(turn).isPlaying()) {
      return false;
    }
    Available a = new Available(g, turn);
    Map<Square, Corner> corners = a.cornerTypes();
    Set<Shape> pieces = g.getPlayer(turn).getRemainingPieces();
    if (corners.isEmpty()) {
      return false;
    }
    // Common case: player has I1
    if (pieces.contains(Shape.I1)) {
      return true;
    }
    // Common case: player has I2
    if (pieces.contains(Shape.I2)) {
      for (Square s : a.corners()) {
        if (a.available(s.translate(-1, 0))
          || a.available(s.translate(1, 0))
          || a.available(s.translate(0, -1))
          || a.available(s.translate(0, 1))) {
          return true;
        }
      }
      return false;
    }
    // General case
    Map<Square, Integer> cornerSizes = a.cornerSizes(5);
    for (Shape shape : pieces) {
      for (Square s : a.corners()) {
        if (cornerSizes.get(s) >= shape.size()) {
          for (RelativeMove r : relativeMoves.get(corners.get(s)).get(shape)) {
            Move m = r.translate(s.getX(), s.getY());
            if (isAvailable(m, a)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Gets a random legal move for the player with the given turn.
   * 
   * @param g game
   * @param turn turn
   * @return random move
   * @throws IllegalStateException if player has no legal moves
   */
  public static Move randomMove(Game g, Turn turn) {
    List<Move> legalMoves = legalMoves(g, turn);
    if (legalMoves.isEmpty()) {
      throw new IllegalStateException(String.format(
        "%s player has no moves in game %s", turn, g));
    }
    return legalMoves.get(new Random().nextInt(legalMoves.size()));
  }
  
  /**
   * Returns an array that represents whether each piece is playable for the
   * player with the given turn.  The ordering of the array is the same as the
   * ordering of the {@link Shape} enum.
   * 
   * @param g game
   * @param turn turn
   * @return playable pieces
   */
  public static boolean[] playablePieces(Game g, Turn turn) {
    boolean[] playable = new boolean[Shape.NUM_SHAPES];
    if (!g.getPlayer(turn).isPlaying()) {
      return playable;
    }
    Available a = new Available(g, turn);
    Map<Square, Corner> corners = a.cornerTypes();
    Map<Square, Integer> cornerSizes = a.cornerSizes(5);
    Shape[] shapes = Shape.values();
    eachShape:
    for (int i = 0; i < shapes.length; i++) {
      Shape shape = shapes[i];
      if (!g.getPlayer(turn).hasPiece(shape)) {
        continue;
      }
      for (Square s : a.corners()) {
        if (shape == I1) {
          playable[i] = true;
          continue eachShape;
        }
        if (cornerSizes.get(s) >= shape.size()) {
          for (RelativeMove r : relativeMoves.get(corners.get(s)).get(shape)) {
            Move m = r.translate(s.getX(), s.getY());
            if (isAvailable(m, a)) {
              playable[i] = true;
              continue eachShape;
            }
          }
        }
      }
    }
    return playable;
  }
  
  /**
   * Returns playable corners for the player with the given turn.  A corner is
   * playable if the player has some legal move that covers the corner.
   * 
   * @param g game
   * @param turn turn
   * @return playable corners
   */
  public static Set<Square> playableCorners(Game g, Turn turn) {
    if (!g.getPlayer(turn).isPlaying()) {
      return Collections.emptySet();
    }
    Available a = new Available(g, turn);
    Map<Square, Corner> cornerTypes = a.cornerTypes();
    Map<Square, Integer> cornerSizes = a.cornerSizes(5);
    Set<Square> corners = a.corners();
    Set<Shape> pieces = g.getPlayer(turn).getRemainingPieces();
    // Common case: player has I1
    if (pieces.contains(I1)) {
      return corners;
    }
    Set<Square> playable = new HashSet<>();
    // Common case: player has I2
    if (pieces.contains(I2)) {
      for (Square c : corners) {
        if (cornerSizes.get(c) > 1) {
          playable.add(c);
        }
      }
      return playable;
    }
    // General case
    eachCorner:
    for (Square s : a.corners()) {
      for (Shape shape : pieces) {
        if (cornerSizes.get(s) >= shape.size()) {
          for (RelativeMove r : relativeMoves.get(cornerTypes.get(s)).get(shape)) {
            Move m = r.translate(s.getX(), s.getY());
            if (isAvailable(m, a)) {
              playable.add(s);
              continue eachCorner;
            }
          }
        }
      }
    }
    return playable;
  }
}
