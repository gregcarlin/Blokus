package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;

/**
 * An AI can suggest a move for the turn player in a game.
 * 
 * @author aaronzhang
 */
@FunctionalInterface
public interface AI {
  /**
   * Suggests a move for the turn player in the game.
   * 
   * @param game game
   * @return move
   */
  Move suggestMove(Game game);
}