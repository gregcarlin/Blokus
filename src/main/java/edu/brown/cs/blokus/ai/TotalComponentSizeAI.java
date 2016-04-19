package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import java.util.HashSet;
import java.util.Set;

/**
 * AI that makes a move based on total component size.  Total component size is
 * the sum of the sizes of all components that contain one of the turn player's
 * corners, but components with multiple corners are counted multiple times.
 * 
 * @author aaronzhang
 */
public class TotalComponentSizeAI implements AI {

  @Override
  public Move suggestMove(Game game) {
    return bestMove(game, game.getTurn());
  }
  
  /**
   * Total component size for player with given turn.
   * 
   * @param g game
   * @param turn turn
   * @return total component size
   */
  public int totalComponentSize(Game g, Turn turn) {
    Set<Square> componentSquares = new HashSet<>();
    Set<Square> availableCorners = Evaluator.getAvailableCorners(g, turn);
    for (Square corner : availableCorners) {
      if (!componentSquares.contains(corner)) {
        componentSquares.addAll(Evaluator.component(g, turn, corner));
      }
    }
    return componentSquares.size();
  }

  /**
   * Best move based on total component size.
   * 
   * @param g game
   * @param turn turn
   * @return best move
   */
  public Move bestMove(Game g, Turn turn) {
    Move bestMove = null;
    int bestValue = -1;
    for (Move m : g.getLegalMoves(turn)) {
      int value = g.tryMove(m,
        g2 -> totalComponentSize(g2, turn) + 2 * m.getSquares().size());
      if (value > bestValue) {
        bestMove = m;
        bestValue = value;
      }
    }
    return bestMove;
  }
}