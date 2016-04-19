package edu.brown.cs.blokus.ai;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.Turn;
import java.util.EnumMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
  
  /**
   * Simulates a game using the given type of AI for each player.
   * 
   * @param ai1 AI for first player
   * @param ai2 AI for second player
   * @param ai3 AI for third player
   * @param ai4 AI for fourth player
   */
  static void simulateGame(Supplier<AI> ai1, Supplier<AI> ai2, Supplier<AI> ai3,
    Supplier<AI> ai4) {
    Game g = new Game.Builder().setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("1"))
      .player(Turn.SECOND, new Player("2"))
      .player(Turn.THIRD, new Player("3"))
      .player(Turn.FOURTH, new Player("4"))
      .timer(0).build()).build();
    EnumMap<Turn, AI> ais = new EnumMap<>(Turn.class);
    ais.put(Turn.FIRST, ai1.get());
    ais.put(Turn.SECOND, ai2.get());
    ais.put(Turn.THIRD, ai3.get());
    ais.put(Turn.FOURTH, ai4.get());
    Turn turn;
    while ((turn = g.getTurn()) != null) {
      g.makeMove(ais.get(turn).suggestMove(g));
    }
    System.out.println(g.getBoard());
    System.out.println(g.getAllPlayers().stream()
      .map(p -> p.getScore()).collect(Collectors.toList()));
  }
  
  /**
   * Simulates a game using the same type of AI for all four players.
   * 
   * @param ai AI supplier
   */
  static void simulateGame(Supplier<AI> ai) {
    simulateGame(ai, ai, ai, ai);
  }
}