package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class ShapeTest {

  @Test
  public void shapeTest() {
    /* This is a test for getSquares(), contains(), and size();
      We start by creating a shape. Then we create a set of
      squares which should be part of the shape we just created.
      Then we call getSquares() on the shape to get a set of squares.
      A list of booleans is created, then we loop through the set of
      squares we manually created to check if it is contained in the
      shape, storing the results in the list of boolean. All of the elements
      of that list should be true. We also test the size and getSquares();
    */

    //---------------------------------------
    // I1
    /*We start by creating a shape.*/
    Shape shape = Shape.I1;
    /*Then we create a set of
    squares which should be part of the shape we just created.
    */
    Set<Square> squares = new HashSet<Square>();
    Square sq = new Square(0,0);
    squares.add(sq);

    /*Then we call getSquares() on the shape to get a set of squares.
    */
    Set<Square> result = shape.getSquares();
    //Iterator iter = result.iterator();

    /*A list of booleans is created, then we loop through the set of
    squares we mantually created to check if it is contained in the
    shape, storing the results in the list of boolean.
    */
    List<Boolean> truth = new ArrayList<Boolean>();
    for (Square s : squares) {
      truth.add(shape.contains(s));
    }

    /*All of the elements
    of that list should be true.
    */
    Boolean passed = true;
    for (Boolean t : truth) {
      if (t == false) {
        passed = false;
        break;
      }
    }
    /*We also test the size and getSquares()
    */
    assertEquals(passed, true);
    assertEquals(result.size(), 1);
    assertEquals(result, squares);

    //--------------------------------------------------
    // I3
    Shape shape2 = Shape.I3;
    Set<Square> squares1 = new HashSet<Square>();
    Square sq1 = new Square(0,0);
    Square sq2 = new Square(-1,0);
    Square sq3 = new Square(1,0);
    squares1.add(sq1);
    squares1.add(sq2);
    squares1.add(sq3);

    Set<Square> result2 = shape2.getSquares();
 //   Iterator iter2 = result2.iterator();
    List<Boolean> truth2 = new ArrayList<Boolean>();

    for (Square s : squares1) {
      truth2.add(shape2.contains(s));
    }

    Boolean passed2 = true;
    for (Boolean t : truth2) {
      if (t == false) {
        passed2 = false;
        break;
      }
    }
    assertEquals(passed2, true);
    assertEquals(result2.size(), 3);
    assertEquals(result2, squares1);

    //--------------------------------------------------------
    // O4
    Shape shape3 = Shape.O4;
    Set<Square> squares2 = new HashSet<Square>();
    Square sq4 = new Square(0,0);
    Square sq5 = new Square(0,1);
    Square sq6 = new Square(1,0);
    Square sq7 = new Square(1,1);
    squares2.add(sq4);
    squares2.add(sq5);
    squares2.add(sq6);
    squares2.add(sq7);
    
    Set<Square> result3 = shape3.getSquares();
  //  Iterator iter3 = result3.iterator();
    List<Boolean> truth3 = new ArrayList<Boolean>();

    for (Square s : squares2) {
      truth3.add(shape3.contains(s));
    }

    Boolean passed3 = true;
    for (Boolean t : truth3) {
      if (t == false) {
        passed3 = false;
        break;
      }
    }
    assertEquals(passed3, true);
    assertEquals(result3.size(), 4);
    assertEquals(result3, squares2);


  }
  
  /**
   * Test that the symmetries are correct.  By correct, we mean that no
   * distinct orientations are left out.
   */
  @Test
  public void testSymmetries() {
    Set<Set<Square>> moves = new HashSet<>();
    // The set of moves using the distinct orientations for each shape...
    for (Shape s : Shape.values()) {
      for (Orientation o : s.distinctOrientations()) {
        for (int x = 0; x < Board.DEFAULT_SIZE; x++) {
          for (int y = 0; y < Board.DEFAULT_SIZE; y++) {
            moves.add(new Move(s, o, x, y).getSquares());
          }
        }
      }
    }
    // should include all moves using _any_ orientation for each shape that
    // don't go off the board
    for (Shape s : Shape.values()) {
      for (Orientation o : Orientation.values()) {
        for (int x = 0; x < Board.DEFAULT_SIZE; x++) {
          for (int y = 0; y < Board.DEFAULT_SIZE; y++) {
            Set<Square> move = new Move(s, o, x, y).getSquares();
            assertTrue(moves.contains(move)
              || move.stream().anyMatch(q ->
              q.getX() < 0 || q.getX() >= Board.DEFAULT_SIZE
              || q.getY() < 0|| q.getY() >= Board.DEFAULT_SIZE));
          }
        }
      }
    }
  }
}
