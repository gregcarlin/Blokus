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
      .player(Turn.FOURTH, new Player("four"))
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
      .player(Turn.SECOND, new Player("two2", game1Shapes, 0, true))
      .player(Turn.THIRD, new Player("three2"))
      .player(Turn.FOURTH, new Player("fourth2"))
      .build())
    .setTurn(Turn.SECOND)
    .setBoard(board3).build();
    
    
    // game 4
    Board board4 = new Board();
    board4.setXY(5, 15, 1);
    board4.setXY(15, 15, 2);
    board4.setXY(14, 14, 3);
    board4.setXY(14, 16, 3);
    board4.setXY(16, 14, 3);
    board4.setXY(16, 16, 3);
    game4 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one", game1Shapes, 0, true))
      .player(Turn.SECOND, new Player("two", game1Shapes, 0, true))
      .player(Turn.THIRD, new Player("three", game1Shapes, 0, true))
      .player(Turn.FOURTH, new Player("four"))
      .build())
    .setBoard(board4).build();

 // game 5
    Board board5 = new Board();
    board5.setXY(10, 10, 2);
    board5.setXY(9, 9, 3);
    board5.setXY(9, 11, 3);
    board5.setXY(11, 9, 3);
    board5.setXY(15, 11, 3);
    board5.setXY(11, 12, 3);
    game5 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one5", EnumSet.noneOf(Shape.class), 0, true))
      .player(Turn.SECOND, new Player("two5", EnumSet.of(Shape.I5), 0, true))
      .player(Turn.THIRD, new Player("three5"))
      .player(Turn.FOURTH, new Player("four5"))
      .build())
    .setBoard(board5)
    .build();
    
    
  }

  /**
   * Some general tests for {@link Game#isLegal(Move)}. A sample of some
   * instances where a move may be legal or illegal, but doesn't include cases
   * where a move would be illegal because the player doesn't have the piece.
   */
  @Test
  public void testIsLegal() {
//    // Should be illegal: piece goes off edge of board
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 0, 0)));
//    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.F5, Orientation.E, 3, 4)));
//    // Should be illegal: piece is on an occupied square
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 1, 2)));
//    // Should be illegal: piece touches own color at edge
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 10, 8)));
//    // Should be illegal: piece doesn't touch own color at any corner
    assertFalse(game1.isLegal(new Move(Shape.F5, Orientation.E, 15, 15)));
//    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.F5, Orientation.E, 11, 8)));
//    // Should be legal
    assertTrue(game1.isLegal(new Move(Shape.N5, Orientation.S, 12, 11)));
    
    
//  //GAME 2
    assertTrue(game2.isLegal(new Move(Shape.I5, Orientation.E, 2, 19)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 4, 3)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 7, 3)));
    assertFalse(game2.isLegal(new Move(Shape.I5, Orientation.E, 8, 9)));
//
//    //Game 3
    assertTrue(game3.isLegal(new Move(Shape.U5, Orientation.E, 11, 16)));
    assertTrue(game3.isLegal(new Move(Shape.P5, Orientation.E, 16, 10)));
    assertTrue(game3.isLegal(new Move(Shape.L4, Orientation.E, 12, 6)));
    assertFalse(game3.isLegal(new Move(Shape.L5, Orientation.E, 6, 5)));
    assertFalse(game3.isLegal(new Move(Shape.Z4, Orientation.E, 8, 8)));
    assertFalse(game3.isLegal(new Move(Shape.O4, Orientation.E, 9, 9)));
    assertFalse(game3.isLegal(new Move(Shape.I5, Orientation.E, 15,8)));
  }

  @Test
  public void testCanMove() {
//	    // Game 1
      assertTrue(game1.canMove(Turn.FIRST));
      assertTrue(game1.canMove(Turn.SECOND));
      assertTrue(game1.canMove(Turn.THIRD));
      assertTrue(game1.canMove(Turn.FOURTH));
      
      assertTrue(game4.canMove(Turn.FIRST));
      assertFalse(game4.canMove(Turn.SECOND));
      assertTrue(game4.canMove(Turn.THIRD));
      assertTrue(game4.canMove(Turn.FOURTH));
      
      assertFalse(game5.canMove(Turn.FIRST));
      assertFalse(game5.canMove(Turn.SECOND));
      assertTrue(game5.canMove(Turn.THIRD));
      assertTrue(game5.canMove(Turn.FOURTH));
  }
  
  @Test
  public void testMakeMove() {
	  Board board6 = new Board();
    Game game6 = new Game.Builder()
    .setSettings(new GameSettings.Builder()
      .player(Turn.FIRST, new Player("one1"))
      .player(Turn.SECOND, new Player("two1"))
      .player(Turn.THIRD, new Player("three"))
      .player(Turn.FOURTH, new Player("four"))
      .build())
    .setBoard(board6).build();
    assertEquals(board6.getXY(0, 19), 0);
    assertEquals(board6.getXY(19, 19), 0);
    assertEquals(board6.getXY(19, 0), 0);
    assertEquals(board6.getXY(0, 0), 0);
    game6.makeMove(game6.getRandomMove(game6.getTurn()));
    assertEquals(board6.getXY(0, 19), 1);
    game6.makeMove(game6.getRandomMove(game6.getTurn()));
    assertEquals(board6.getXY(19, 19), 2);
    game6.makeMove(game6.getRandomMove(game6.getTurn()));
    assertEquals(board6.getXY(19, 0), 3);
    game6.makeMove(game6.getRandomMove(game6.getTurn()));
    assertEquals(board6.getXY(0, 0), 4);
  }
}
