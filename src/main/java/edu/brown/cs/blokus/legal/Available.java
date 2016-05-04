package edu.brown.cs.blokus.legal;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Available squares and corners for one player.
 *
 * @author aaronzhang
 */
public class Available {

  /**
   * Game.
   */
  private final Game g;

  /**
   * Turn.
   */
  private final Turn turn;

  /**
   * Whether it is the player's first turn.
   */
  private final boolean firstTurn;

  /**
   * Board size.
   */
  private final int boardSize;

  /**
   * Array of available squares, by x,y-coordinates (not row-column).
   */
  private final boolean[][] available;

  /**
   * Squares occupied by the player.
   */
  private final Set<Square> places = new HashSet<>();

  /**
   * Corner values for the player.
   */
  private final Map<Square, Integer> cornerValues = new HashMap<>();

  /**
   * Corner types for the player.
   */
  private final Map<Square, Corner> cornerTypes = new HashMap<>();

  /**
   * Memoize {@link Available#cornerSizes(int)}.
   */
  private final Map<Integer, Map<Square, Integer>> cornerSizes = new HashMap<>();

  /**
   * The size of the available array is the size of the board, surrounded by a
   * rectangular border.
   */
  private static final int BORDER = Shape.MAX_RADIUS;

  /**
   * Calculates available squares and corners for the turn player.
   *
   * @param g game
   * @param turn turn
   */
  public Available(Game g, Turn turn) {
    this.g = g;
    this.turn = turn;
    firstTurn
      = g.getPlayer(turn).getRemainingPieces().size() == Shape.NUM_SHAPES;
    boardSize = g.getBoard().size();
    available = new boolean[boardSize + 2 * BORDER][boardSize + 2 * BORDER];
    calculateAvailable();
    calculateCorners();
  }

  /**
   * Fills in availability array.
   */
  private void calculateAvailable() {
    for (int x = 0; x < boardSize; x++) {
      for (int y = 0; y < boardSize; y++) {
        available[x + BORDER][y + BORDER] = true;
      }
    }
    for (int x = 0; x < boardSize; x++) {
      for (int y = 0; y < boardSize; y++) {
        int mark = g.getXY(x, y);
        if (mark == turn.mark()) {
          places.add(new Square(x, y));
          available[x + BORDER][y + BORDER] = false;
          available[x + BORDER - 1][y + BORDER] = false;
          available[x + BORDER + 1][y + BORDER] = false;
          available[x + BORDER][y + BORDER - 1] = false;
          available[x + BORDER][y + BORDER + 1] = false;
        } else if (mark != 0) {
          available[x + BORDER][y + BORDER] = false;
        }
      }
    }
  }

  /**
   * Whether the given square is available. An available square is unoccupied
   * and does not share an edge with a square of the same color.
   *
   * @param x x
   * @param y y
   * @return availability
   */
  public boolean available(int x, int y) {
    return available[x + BORDER][y + BORDER];
  }

  /**
   * Whether the given square is available. An available square is unoccupied
   * and does not share an edge with a square of the same color.
   *
   * @param s square
   * @return availability
   */
  public boolean available(Square s) {
    return available(s.getX(), s.getY());
  }

  /**
   * @return squares occupied by the player
   */
  public Set<Square> places() {
    return Collections.unmodifiableSet(places);
  }

  /**
   * Calculates corners.
   */
  private void calculateCorners() {
    if (firstTurn) {
      Square corner = g.getCorner(turn);
      if (available(corner)) {
        cornerValues.put(corner, startingCorner().getValue());
      }
    } else {
      for (Square s : places) {
        checkCorner(cornerValues, s.translate(1, 1), Corner.BOTTOM_LEFT);
        checkCorner(cornerValues, s.translate(1, -1), Corner.TOP_LEFT);
        checkCorner(cornerValues, s.translate(-1, 1), Corner.BOTTOM_RIGHT);
        checkCorner(cornerValues, s.translate(-1, -1), Corner.TOP_RIGHT);
      }
    }
    for (Square s : cornerValues.keySet()) {
      cornerTypes.put(s, Corner.fromValue(cornerValues.get(s)));
    }
  }

  /**
   * @return starting corner type
   */
  private Corner startingCorner() {
    switch (turn) {
      case FIRST:
        return Corner.TOP_LEFT;
      case SECOND:
        return Corner.TOP_RIGHT;
      case THIRD:
        return Corner.BOTTOM_RIGHT;
      case FOURTH:
        return Corner.BOTTOM_LEFT;
      default:
        throw new AssertionError();
    }
  }

  /**
   * If the given square is available, updates the map of corner values.
   *
   * @param cornerValues corner values
   * @param s square
   * @param cornerType corner type
   */
  private void checkCorner(Map<Square, Integer> cornerValues, Square s,
    Corner cornerType) {
    if (available(s)) {
      cornerValues.put(s,
        cornerValues.getOrDefault(s, 0) + cornerType.getValue());
    }
  }

  /**
   * @return corner values
   */
  public Map<Square, Integer> cornerValues() {
    return Collections.unmodifiableMap(cornerValues);
  }

  /**
   * @return corner types
   */
  public Map<Square, Corner> cornerTypes() {
    return Collections.unmodifiableMap(cornerTypes);
  }

  /**
   * @return corners
   */
  public Set<Square> corners() {
    return Collections.unmodifiableSet(cornerValues.keySet());
  }

  /**
   * Size of component containing the given square, up to the given limit.
   *
   * @param s square
   * @param limit limit
   * @return component size, up to limit
   */
  public int componentSize(Square s, int limit) {
    Set<Square> comp = new HashSet<>();
    Queue<Square> search = new LinkedList<>();
    if (available(s)) {
      search.add(s);
    }
    while (!search.isEmpty()) {
      Square current = search.poll();
      comp.add(current);
      if (comp.size() >= limit) {
        return limit;
      }
      search(comp, search, current.translate(-1, 0));
      search(comp, search, current.translate(1, 0));
      search(comp, search, current.translate(0, -1));
      search(comp, search, current.translate(0, 1));
    }
    return comp.size();
  }

  private void search(Set<Square> comp, Queue<Square> search, Square s) {
    if (!comp.contains(s) && available(s)) {
      search.add(s);
    }
  }

  /**
   * Gets the size of the component at each corner, up to the given limit.
   * This function is memoized, so calling this function again with the same
   * limit requires no additional computation.
   *
   * @param limit limit
   * @return size of component at each corner
   */
  public Map<Square, Integer> cornerSizes(int limit) {
    return cornerSizes.computeIfAbsent(limit, l -> {
      Map<Square, Integer> sizes = new HashMap<>();
      for (Square c : corners()) {
        sizes.put(c, componentSize(c, l));
      }
      return sizes;
    });
  }
}
