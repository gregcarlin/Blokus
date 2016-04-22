package edu.brown.cs.blokus;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Stores information about a player: remaining pieces, score, and whether the
 * player is still playing.
 *
 * @author aaronzhang
 */
public class Player {

  /**
   * ID.
   */
  private final String id;

  /**
   * Remaining pieces.
   */
  private final Set<Shape> remainingPieces;

  /**
   * Score.
   */
  private int score;

  /**
   * Whether the player is still playing. Once the player has no more legal
   * moves, this flag is set to false.
   */
  private boolean playing;

  /**
   * Bonus for using all pieces.
   */
  private static final int BONUS_ALL_PIECES = 15;

  /**
   * Bonus for using the one-piece last.
   */
  private static final int BONUS_ONE_LAST = 5;

  /**
   * Instantiates player with remaining pieces, score, and whether the player is
   * still playing.
   *
   * @param id the player's unique id, or null if this player is new
   * @param remainingPieces remaining pieces
   * @param score score
   * @param playing whether the player is still playing
   */
  public Player(String id, Collection<Shape> remainingPieces, int score,
    boolean playing) {
    this.id = id;
    this.remainingPieces = remainingPieces.size() == 0
      ? Collections.emptySet() : EnumSet.copyOf(remainingPieces);
    this.score = score;
    this.playing = playing;
  }

  /**
   * Instantiates a player representing a player at the start of the game. At
   * the start of the game, the player has all pieces and a score of 0.
   *
   * @param id the player's unique id, or null if this player is new
   */
  public Player(String id) {
    this(id, EnumSet.allOf(Shape.class), 0, true);
  }

  /**
   * @return remainingPieces
   */
  public Set<Shape> getRemainingPieces() {
    return Collections.unmodifiableSet(remainingPieces);
  }

  /**
   * Whether this player has the piece.
   *
   * @param piece piece
   * @return whether this player has the piece
   */
  public boolean hasPiece(Shape piece) {
    return remainingPieces.contains(piece);
  }

  /**
   * Removes piece from set of remaining pieces.
   *
   * @param piece piece
   */
  public void removePiece(Shape piece) {
    remainingPieces.remove(piece);
  }

  /**
   * Removes piece from set of remaining pieces and adds to score.
   *
   * @param piece piece
   */
  public void usePiece(Shape piece) {
    removePiece(piece);
    addScore(piece.size());
    if (remainingPieces.isEmpty()) {
      addScore(BONUS_ALL_PIECES);
      if (piece == Shape.I1) {
        addScore(BONUS_ONE_LAST);
      }
    }
  }

  /**
   * Undoes a move.
   *
   * @param piece piece
   */
  public void undoUsePiece(Shape piece) {
    remainingPieces.add(piece);
    score -= piece.size();
  }

  /**
   * @return the score
   */
  public int getScore() {
    return score;
  }

  /**
   * Adds to score.
   *
   * @param add how much to add
   */
  public void addScore(int add) {
    score += add;
  }

  /**
   * @return whether the player is still playing
   */
  public boolean isPlaying() {
    return playing;
  }

  /**
   * The player is no longer playing.
   */
  public void stopPlaying() {
    playing = false;
  }

  /**
   * Gets this player's unique id.
   *
   * @return the of id this player as a string
   */
  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return String.format(
      "[Player: id=%s, remainingPieces=%s, score=%s]",
      id, remainingPieces, score);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Player)) {
      return false;
    }

    Player other = (Player) obj;
    return Objects.equals(id, other.id)
      && remainingPieces.equals(other.remainingPieces)
      && score == other.score;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, remainingPieces, score, playing);
  }
}
