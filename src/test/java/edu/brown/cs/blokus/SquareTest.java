package edu.brown.cs.blokus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;



public class SquareTest {


  @Test
  public void translateTest() {
  /* This is a test of the translate(int, int) function.
     We first create two lists of squares. The first list
     of squares will have normal squares. The second list
     will have the transated versions of the squares in the first
     list. We then go through the two lists and compare them, placing
     the boolean results in a list. By looking through the list 
     of booleans for make sure they are all true, we show that the translate
     fucntion works properly.
  */

  /*We first create two lists of squares.*/
  List<Square> squares = new ArrayList<Square>();
  List<Square> translated = new ArrayList<Square>();


    /*The first list
    of squares will have normal squares.
    */
    for (int i = 0; i < 6; i++) {
      int x = i;
      int y = i + 2;
      squares.add(new Square(x, y));
      }
    /*The second list
    will have the transated versions of the squares in the first
    list.
    */
    for (int i = 0; i < 6; i++) {
      int x = i + 3;
      int y = i + 3;
      translated.add(new Square(x, y));
    }


    /*We then go through the two lists and compare them, placing
    the boolean results in a list.
    */
    List<Boolean> truth = new ArrayList<Boolean>();
    for (int i = 0; i < 6; i++) {
      truth.add(squares.get(i).translate(3, 1).equals(translated.get(i)));
    }

    /*By looking through the list
    of booleans to make sure they are all true, we show that the translate
    fucntion works properly.
    */
    Boolean passed = true;
    for (Boolean b : truth) {
      if (b == false) {
        passed = false;
        break;
      }
    }
    assert(passed);
  }


  @Test
  public void getXYTest() {
    /*We first create a list of squares with x and y values.
    The we go through the list of squares and check if getX() and
    getY() return the right values.
    */
    List<Square> squares = new ArrayList<Square>();
      for (int i = 0; i < 5; i++) {
        int x = i;
        int y = i + 1;
        squares.add(new Square(x, y));
      }

    List<Boolean> truth = new ArrayList<Boolean>();
    for (int i = 0; i < 5; i++) {
      truth.add(((squares.get(i).getX() == i) && (squares.get(i).getY() == i+1)));
    }

    Boolean passed = true;
    for (Boolean b : truth) {
      if (b == false) {
        passed = false;
        break;
      }
    }
    assert(passed);

  }






}
