package edu.brown.cs.blokus;

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
    game1 = new Game.Builder()
      .setSettings(new GameSettings.Builder().build())
      .setBoard(board1).build();
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
}
