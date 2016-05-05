package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;

/**
 * An AI that makes random moves.
 *
 * @author aaronzhang
 */
public class RandomAI implements AI {
  @Override
  public Move suggestMove(Game game) {
    return game.getRandomMove(game.getTurn());
  }
}
