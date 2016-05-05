package edu.brown.cs.blokus;

/**
 * A turn in a four-player Blokus game: first, second, third, or fourth.
 *
 * @author aaronzhang
 */
public enum Turn {

  FIRST() {
    @Override
    public Turn next() {
      return SECOND;
    }
    @Override
    public String toString() {
      return "first";
    }
  },
  SECOND() {
    @Override
    public Turn next() {
      return THIRD;
    }
    @Override
    public String toString() {
      return "second";
    }
  },
  THIRD() {
    @Override
    public Turn next() {
      return FOURTH;
    }
    @Override
    public String toString() {
      return "third";
    }
  },
  FOURTH() {
    @Override
    public Turn next() {
      return FIRST;
    }
    @Override
    public String toString() {
      return "fourth";
    }
  };

  /**
   * Gets the next turn.
   *
   * @return next turn
   */
  public abstract Turn next();

  /**
   * The mark on the board representing the turn. For example,
   * {@link Turn#FIRST} has the mark 1.
   *
   * @return mark
   */
  public int mark() {
    return ordinal() + 1;
  }
}
