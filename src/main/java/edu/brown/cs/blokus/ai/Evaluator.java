package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Static methods that evaluate a game. These methods can be used by AIs.
 *
 * @author aaronzhang
 */
public class Evaluator {
  private Evaluator() {
  }

  /**
   * Gets the squares occupied by the player with the given turn.
   *
   * @param g game
   * @param turn turn
   * @return squares occupied by player
   */
  public static Set<Square> getPlaces(Game g, Turn turn) {
    Set<Square> places = new HashSet<>();
    final int boardSize = g.getBoard().size();
    final int mark = turn.mark();
    for (int x = 0; x < boardSize; x++) {
      for (int y = 0; y < boardSize; y++) {
        if (g.getBoard().getXY(x, y) == mark) {
          places.add(new Square(x, y));
        }
      }
    }
    return places;
  }

  /**
   * Gets squares that share a corner with any square occupied by the player
   * with the given turn.
   *
   * @param g game
   * @param turn turn
   * @return corner squares
   */
  public static Set<Square> getCorners(Game g, Turn turn) {
    Set<Square> corners = new HashSet<>();
    Set<Square> places = getPlaces(g, turn);
    for (Square place : places) {
      corners.add(place.translate(-1, -1));
      corners.add(place.translate(-1, 1));
      corners.add(place.translate(1, -1));
      corners.add(place.translate(1, 1));
    }
    corners.removeAll(places);
    return corners;
  }

  /**
   * Gets a matrix of available squares. The matrix size is 2 greater than the
   * board size; there is an extra row on the top and bottom and an extra column
   * on the left and right. Each matrix entry is true if the square could be
   * occupied by the player in the future, and false if the square cannot ever
   * be occupied by the player.
   *
   * @param g game
   * @param turn turn
   * @return availability matrix
   */
  public static boolean[][] available(Game g, Turn turn) {
    final int boardSize = g.getBoard().size();
    final int mark = turn.mark();
    boolean[][] available = new boolean[boardSize + 2][boardSize + 2];
    for (int r = 1; r <= boardSize; r++) {
      for (int c = 1; c <= boardSize; c++) {
        available[r][c] = true;
      }
    }
    for (int r = 1; r <= boardSize; r++) {
      for (int c = 1; c <= boardSize; c++) {
        int boardMark = g.getBoard().getRowColumn(r - 1, c - 1);
        if (boardMark == mark) {
          available[r][c] = false;
          available[r + 1][c] = false;
          available[r - 1][c] = false;
          available[r][c + 1] = false;
          available[r][c - 1] = false;
        } else if (boardMark != 0) {
          available[r][c] = false;
        }
      }
    }
    return available;
  }

  /**
   * Gets available corners. Available corners are corners where the player
   * would be able to play the 1-piece, if the player has it.
   *
   * @param g game
   * @param turn turn
   * @return available corners
   */
  public static Set<Square> getAvailableCorners(Game g, Turn turn) {
    boolean[][] available = available(g, turn);
    Set<Square> corners = getCorners(g, turn);
    Iterator<Square> cornersItr = corners.iterator();
    while (cornersItr.hasNext()) {
      Square s = cornersItr.next();
      /*
      WRONG
      */
      if (!available[s.getX() + 1][s.getY() + 1]) {
        cornersItr.remove();
      }
    }
    return corners;
    /*
     boolean[][] available = available(g, turn);
     Set<Square> corners = getCorners(g, turn);
     corners.removeIf(s -> !available[s.getX() + 1][s.getY() + 1]);
     return corners;
     */
  }

  public static Set<Square> getAvailableCorners(Game g, Turn turn,
    boolean[][] available) {
    Set<Square> corners = getCorners(g, turn);
    Iterator<Square> cornersItr = corners.iterator();
    while (cornersItr.hasNext()) {
      Square s = cornersItr.next();
      if (!available[s.getX() + 1][s.getY() + 1]) {
        cornersItr.remove();
      }
    }
    return corners;
  }

  /**
   * Gets the component at the given square. A component is a set of squares
   * connected horizontally or vertically that are all available to the given
   * player (as determined by {@link Evaluator#available(Game, Turn)}.
   *
   * @param g game
   * @param turn turn
   * @param square a square in the component
   * @return the whole component
   */
  public static Set<Square> component(Game g, Turn turn, Square square) {
    boolean[][] available = available(g, turn);
    Set<Square> comp = new HashSet<>();
    Queue<Square> toSearch = new LinkedList<>();
    checkAvailable(available, comp, toSearch, square);
    while (!toSearch.isEmpty()) {
      Square current = toSearch.poll();
      checkAvailable(available, comp, toSearch, current.translate(-1, 0));
      checkAvailable(available, comp, toSearch, current.translate(1, 0));
      checkAvailable(available, comp, toSearch, current.translate(0, -1));
      checkAvailable(available, comp, toSearch, current.translate(0, 1));
    }
    return comp;
  }

  public static Set<Square> component(Game g, Turn turn, Square square,
    boolean[][] available) {
    Set<Square> comp = new HashSet<>();
    Queue<Square> toSearch = new LinkedList<>();
    checkAvailable(available, comp, toSearch, square);
    while (!toSearch.isEmpty()) {
      Square current = toSearch.poll();
      checkAvailable(available, comp, toSearch, current.translate(-1, 0));
      checkAvailable(available, comp, toSearch, current.translate(1, 0));
      checkAvailable(available, comp, toSearch, current.translate(0, -1));
      checkAvailable(available, comp, toSearch, current.translate(0, 1));
    }
    return comp;
  }

  /**
   * Helper for {@link Evaluator#component(Game, Turn, Square)}.
   *
   * @param available availability matrix
   * @param comp component
   * @param toSearch squares to search
   * @param s square currently being checked
   */
  private static void checkAvailable(boolean[][] available, Set<Square> comp,
    Queue<Square> toSearch, Square s) {
    if (available[s.getX() + 1][s.getY() + 1] && !comp.contains(s)) {
      comp.add(s);
      toSearch.add(s);
    }
  }

  public static int within(Set<Square> squares, int radius,
    boolean[][] available) {
    Multimap<Integer, Square> within = HashMultimap.create();
    within.putAll(0, squares);
    for (int d = 1; d <= radius; d++) {
      for (Square s : within.get(d - 1)) {
        checkAvailableWithin(available, within, d, s.translate(-1, 0));
        checkAvailableWithin(available, within, d, s.translate(1, 0));
        checkAvailableWithin(available, within, d, s.translate(0, -1));
        checkAvailableWithin(available, within, d, s.translate(0, 1));
      }
    }
    return within.size();
  }

  private static void checkAvailableWithin(boolean[][] available,
    Multimap<Integer, Square> within, int d, Square s) {
    if (available[s.getX() + 1][s.getY() + 1] && !within.containsValue(s)) {
      within.put(d, s);
    }
  }
}
