package edu.brown.cs.blokus;

import edu.brown.cs.blokus.ai.AI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

import edu.brown.cs.blokus.ai.TotalComponentSizeAI;

/**
 * Tests for {@link Game}.
 */
public class GameTest {

  /**
   * A game used for some general tests.
   */
  private static Game game1;
  private static Game game2;
  private static Game game3;
  private static Game game4;
  private static Game game5;

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
    
    
 // game 2
    Board board2 = new Board();
    game2 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one1"))
      .player(Turn.SECOND, new Player("two1"))
      .player(Turn.THIRD, new Player("three"))
      .build())
    .setBoard(board2).build();
    
    // game 3
    Board board3 = new Board();
    board3.setXY(9,13,2);
    board3.setXY(8,13,2);
    board3.setXY(9,14,2);
    board3.setXY(10,14,2);

    board3.setXY(14,12,2);
    board2.setXY(13,12,2);
    board3.setXY(12,12,2);
    board3.setXY(15,12,2);
    board3.setXY(15,13,2);

    board3.setXY(9,8,2);
    board3.setXY(8,8,2);
    board3.setXY(8,9,2);
    board3.setXY(10,8,2);

    game3 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one2"))
      .player(Turn.SECOND, new Player("two2"))
      .player(Turn.THIRD, new Player("three2"))
      .player(Turn.FOURTH, new Player("fourth2"))
      .build())
    .setTurn(Turn.SECOND)
    .setBoard(board3).build();
    
    
    // game 4
    Board board4 = new Board();
    game4 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one3"))
      .player(Turn.SECOND, new Player("two3"))
      .build())
    .setBoard(board4).build();

 // game 5
    Board board5 = new Board();
    game5 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one5"))
      .player(Turn.SECOND, new Player("two5"))
      .player(Turn.THIRD, new Player("three5"))
      .player(Turn.FOURTH, new Player("four5"))
      .build())
    .setBoard(board2).build();
    
    
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
    
    
  //GAME 2
    assertTrue(game2.isLegal(new Move(Shape.I5, Orientation.E, 2, 0)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 4, 3)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 7, 3)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 8, 9)));

    //Game 3
    assertTrue(game3.isLegal(new Move(Shape.U5, Orientation.E, 11, 16)));
    assertTrue(game3.isLegal(new Move(Shape.P5, Orientation.E, 16, 10)));
    assertTrue(game3.isLegal(new Move(Shape.L4, Orientation.E, 12, 6)));
    assertTrue(game3.isLegal(new Move(Shape.L5, Orientation.E, 6, 5)));
    assertFalse(game3.isLegal(new Move(Shape.Z4, Orientation.E, 8, 8)));
    assertFalse(game3.isLegal(new Move(Shape.O4, Orientation.E, 9, 9)));
    assertFalse(game3.isLegal(new Move(Shape.I5, Orientation.E, 15,8)));
  }

  @Test
  public void testCanMove() {
	    // Game 1
      assertTrue(game1.canMove(Turn.FIRST));
      assertFalse(game1.canMove(Turn.SECOND));
      assertFalse(game1.canMove(Turn.THIRD));
      assertFalse(game1.canMove(Turn.FOURTH));
      System.out.println(game1.canMove(Turn.FIRST));
      System.out.println(game1.canMove(Turn.SECOND));
      System.out.println(game1.canMove(Turn.THIRD));
      System.out.println(game1.canMove(Turn.FOURTH));
      // Game 2
      assertTrue(game2.canMove(Turn.FIRST));
      assertFalse(game2.canMove(Turn.SECOND));
      assertFalse(game2.canMove(Turn.THIRD));
      assertFalse(game2.canMove(Turn.FOURTH));

      // Game 3 
      assertTrue(game3.canMove(Turn.SECOND));
      assertFalse(game3.canMove(Turn.FIRST));
      assertFalse(game3.canMove(Turn.THIRD));
      assertFalse(game3.canMove(Turn.FOURTH));
  }
  
  @Test
  public void testMakeMove() {
	  // Game 1
	    assertTrue(game1.canMove(Turn.SECOND));
	    game1.makeMove(new Move(Shape.F5, Orientation.E, 3, 4));
	    assertTrue(game1.canMove(Turn.THIRD));

	  // Game 2
	    assertTrue(game2.canMove(Turn.FIRST));
	    game2.makeMove(new Move(Shape.I5, Orientation.E, 2, 0));
	    assertFalse(game2.canMove(Turn.FIRST));
	    assertTrue(game2.canMove(Turn.SECOND));

	  // Game 3
	  assertTrue(game3.canMove(Turn.SECOND));
	  game3.makeMove(new Move(Shape.P5, Orientation.E, 11, 4));
	  assertFalse(game3.canMove(Turn.SECOND));
	  assertFalse(game3.canMove(Turn.THIRD));

  }

  @Test
  public void testPass() {

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
    AI ai = new TotalComponentSizeAI();
    Game g = new Game.Builder().setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("1"))
      .player(Turn.SECOND, new Player("2"))
      .player(Turn.THIRD, new Player("3"))
      .player(Turn.FOURTH, new Player("4")).build()).build();
    while (g.getTurn() != null) {
      g.makeMove(ai.suggestMove(g));
    }
    System.out.println(g.getBoard());
    System.out.println(g.getAllPlayers().stream()
      .map(p -> p.getScore()).collect(Collectors.toList()));
  }
  
  public static void main(String[] args) {
    simulateGameSmart();
  }
}
