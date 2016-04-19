package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;

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
    int sum = 0;
    for (Square s : Evaluator.getCorners(g, turn)) {
      sum += Evaluator.component(g, turn, s).size();
    }
    return sum;
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
      int value = g.tryMove(m, g2 -> totalComponentSize(g2, turn));
      if (value > bestValue) {
        bestMove = m;
        bestValue = value;
      }
    }
    return bestMove;
  }
}