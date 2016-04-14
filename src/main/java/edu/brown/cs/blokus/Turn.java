package edu.brown.cs.blokus;

/**
 * A turn in a four-player Blokus game: first, second, third, or fourth.
 *
 * @author aaronzhang
 */
public enum Turn {

  FIRST() {
        @Override
        public Turn next(int maxPlayers) {
          return maxPlayers > 1 ? SECOND : FIRST;
        }
      },
  SECOND() {
        @Override
        public Turn next(int maxPlayers) {
          return maxPlayers > 2 ? THIRD : FIRST;
        }
      },
  THIRD() {
        @Override
        public Turn next(int maxPlayers) {
          return maxPlayers > 3 ? FOURTH : FIRST;
        }
      },
  FOURTH() {
        @Override
        public Turn next(int maxPlayers) {
          return FIRST;
        }
      };

  /**
   * Gets the next turn.
   *
   * @return next turn
   */
  public abstract Turn next(int maxPlayers);

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
