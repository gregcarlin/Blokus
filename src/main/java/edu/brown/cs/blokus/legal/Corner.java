package edu.brown.cs.blokus.legal;

/**
 * Types of corners. For example, BOTTOM_LEFT means that the occupied square is
 * to the bottom-left of the corner. LEFT means that there are squares on both
 * the bottom-left and top-left of the corner. OPPOSITE means that there are
 * squares opposite from each other--for example, top-left and bottom-right.
 *
 * @author aaronzhang
 */
public enum Corner {

  BOTTOM_LEFT(1),
  TOP_LEFT(2),
  BOTTOM_RIGHT(4),
  TOP_RIGHT(8),
  LEFT(BOTTOM_LEFT.value + TOP_LEFT.value),
  RIGHT(BOTTOM_RIGHT.value + TOP_RIGHT.value),
  BOTTOM(BOTTOM_LEFT.value + BOTTOM_RIGHT.value),
  TOP(TOP_LEFT.value + TOP_RIGHT.value),
  OPPOSITE(LEFT.value + RIGHT.value);

  /**
   * A numerical value used to determine corner types.
   */
  private final int value;

  private Corner(int value) {
    this.value = value;
  }

  /**
   * Converts value to corner type.
   *
   * @param value value
   * @return corner
   */
  public static Corner fromValue(int value) {
    for (Corner c : Corner.values()) {
      if (c.getValue() == value) { return c; }
    }
    return OPPOSITE;
  }

  /**
   * @return numerical value of corner
   */
  public int getValue() {
    return value;
  }
}
