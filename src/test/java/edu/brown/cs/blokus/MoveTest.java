package edu.brown.cs.blokus;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import edu.brown.cs.blokus.Shape;


public class MoveTest {


  @Test
  public void moveTest() {
    /*This is a test for the move class. It will be testing mainly the
      getShape(), getOrientation, getX(), and getY functions. We start
      with creating a Shape, Orientation, x value, y value. Then those
      are used to create a Move object. The Move object is then used to
      call the fucntions named above. The results of those calls are
      compared to the objects used to create the Move object.*/

    /*We start
    with creating a Shape, Orientation, x value, y value.
    */
    Shape shape = Shape.I3;
    Orientation orien = Orientation.E;
    int x = 2;
    int y = 5;
    /*Then those
    are used to create a Move object.
    */
    Move move = new Move(shape, orien, x, y);

    /*The Move object is then used to
    call the fucntions of the Move object.
    */
    Shape resultShape = move.getShape();
    Orientation resultOrien = move.getOrientation();
    int resultX = move.getX();
    int resultY = move.getY();

    /*The results of those calls are
    compared to the objects used to create the Move object.
    */
    assertEquals(shape, resultShape);
    assertEquals(orien, resultOrien);
    assertEquals(x, resultX);
    assertEquals(y, resultY);

    /*We start
    with creating a Shape, Orientation, x value, y value.
    */
    Shape shape2 = Shape.U5;
    Orientation orien2 = Orientation.S;
    int x2 = 1;
    int y2 = 4;
    /*Then those
    are used to create a Move object.
    */
    Move move2 = new Move(shape2, orien2, x2, y2);

    /*The Move object is then used to
    call the fucntions of the Move object.
    */
    Shape resultShape2 = move2.getShape();
    Orientation resultOrien2 = move2.getOrientation();
    int resultX2 = move2.getX();
    int resultY2 = move2.getY();

    /*The results of those calls are
    compared to the objects used to create the Move object.
    */
    assertEquals(shape2, resultShape2);
    assertEquals(orien2, resultOrien2);
    assertEquals(x2, resultX2);
    assertEquals(y2, resultY2);




  }














}
