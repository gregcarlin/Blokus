package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Tests for {@link Game}.
 */
public class GameTest {

  /**
   * A game used for some general tests.
   */
  private static Game game1;

  /**
   * Initialize the test games.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    // game1
    Board board1 = new Board();
    board1.setXY(1, 2, 1);
    board1.setXY(10, 10, 1);
    board1.setXY(11, 10, 2);
    EnumSet<Shape> game1Shapes = EnumSet.allOf(Shape.class);
    game1Shapes.remove(Shape.I1);
    game1 = new Game.Builder()
      .setSettings(new GameSettings.Builder()
        .player(Turn.FIRST, new Player("one", game1Shapes, 0, true))
        .player(Turn.SECOND, new Player("two"))
        .player(Turn.THIRD, new Player("three"))
        .player(Turn.FOURTH, new Player("four"))
        .lastTurnTime(System.currentTimeMillis())
        .build())
      .setBoard(board1)
      .build();
  }

  /**
   * Some general tests for {@link Game#isLegal(Move)}. A sample of some
   * instances where a move may be legal or illegal, but doesn't include cases
   * where a move would be illegal because the player doesn't have the piece.
   */
  @Test
  public void testIsLegal() {
    // Should be illegal: piece goes off edge of board
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 0, 0)));
    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.F5, Orientation.E, 3, 4)));
    // Should be illegal: piece is on an occupied square
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 1, 2)));
    // Should be illegal: piece touches own color at edge
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 10, 8)));
    // Should be illegal: piece doesn't touch own color at any corner
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 15, 15)));
    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.F5, Orientation.E, 11, 8)));
    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.N5, Orientation.S, 12, 11)));
  }

  @Test
  public void testMakeMove() {

  }

  @Test
  public void testPass() {

  }

  @Test
  public void testCanMove() {

  }
  
  /**
   * Simulates game by picking random moves.
   */
  public static void simulateGame() {
    Game g = new Game.Builder().setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("1"))
      .player(Turn.SECOND, new Player("2"))
      .player(Turn.THIRD, new Player("3"))
      .player(Turn.FOURTH, new Player("4")).build()).build();
    while (g.getTurn() != null) {
      g.makeMove(g.getRandomMove(g.getTurn()));
    }
    System.out.println(g.getBoard());
    System.out.println(g.getAllPlayers().stream()
      .map(p -> p.getScore()).collect(Collectors.toList()));
  }
  
  public static void simulateGameSmart() {
    Game g = new Game.Builder().setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("1"))
      .player(Turn.SECOND, new Player("2"))
      .player(Turn.THIRD, new Player("3"))
      .player(Turn.FOURTH, new Player("4")).build()).build();
    while (g.getTurn() != null) {
      g.makeMove(g.bestMove(g.getTurn()));
    }
    System.out.println(g.getBoard());
    System.out.println(g.getAllPlayers().stream()
      .map(p -> p.getScore()).collect(Collectors.toList()));
  }
  
  public static void main(String[] args) {
    simulateGameSmart();
  }
}
