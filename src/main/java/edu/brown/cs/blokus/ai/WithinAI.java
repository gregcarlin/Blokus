package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Square;
import edu.brown.cs.blokus.Turn;
import java.util.Set;

public class WithinAI implements AI {

  @Override
  public Move suggestMove(Game game) {
    return bestMove(game, game.getTurn());
  }
  
  public Move bestMove(Game g, Turn turn) {
    Move bestMove = null;
    int bestValue = -1;
    for (Move m : g.getLegalMoves(turn)) {
      int value = g.tryMove(m, h -> {
        boolean[][] available = Evaluator.available(h, turn);
        Set<Square> corners = Evaluator.getAvailableCorners(h, turn);
        return Evaluator.within(corners, 3, available) + 2 * m.getSquares().size();
      });
      if (value > bestValue) {
        bestMove = m;
        bestValue = value;
      }
    }
    return bestMove;
  }
}