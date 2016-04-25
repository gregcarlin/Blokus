package edu.brown.cs.blokus;




import org.junit.Test;

import static org.junit.Assert.*;
import edu.brown.cs.blokus.Board;

public class BoardTest {
	
	
	

  @Test
  public void rowAndColumTest() {
    /*This is a test to make sure that setRowColumn and
      getRowColun work properly. First a board is created,
      then we use a for loop to create rows, columns, and values
      and give those values to the board. We then check that
      getRowColun returns the value corresponding to the correct
      row and column.
    */

    /*First a board is created,then we use a for loop to create rows,
    columns, and values and give those values to the board.
    */
    Board test = new Board();
    Board outCome = new Board();
    for (int i = 0; i < 5; i++) {
      int row = i;
      int column = i + 2;
      int value = i * 2;

      outCome = test.setRowColumn(row, column, value);
    }

    /*We then check that
    getRowColun returns the value corresponding to the correct
    row and column.
    */
    assertEquals(outCome.getRowColumn(0, 2), 0);
    assertEquals(outCome.getRowColumn(1, 3), 2);
    assertEquals(outCome.getRowColumn(2, 4), 4);
    assertEquals(outCome.getRowColumn(3, 5), 6);
    assertEquals(outCome.getRowColumn(4, 6), 8);
  }

  
  



  @Test
  public void setGetXYTest() {
    /*This is a test to make sure that setXY and getXY
      work as intended. We first create a board. Then we
      use a for loop to create rows, columns, and values,
      and set them to the board. We then make sure that when
      we try to get the value from a row and a column that
      it returns the correct value.
    */

    //We first create a board.
    Board test = new Board();
    Board outCome = new Board();
    /*Then we
    use a for loop to create rows, columns, and values,
    and set them to the board.
    */
    for (int i = 0; i < 5; i++) {
      int row = i;
      int column = i + 2;
      int value = i * 2;
      outCome = test.setXY(row, column, value);
    }

    /*We then make sure that when
    we try to get the value from a row and a column that
    it returns the correct value.
    */
    assertEquals(outCome.getXY(0, 2), 0);
    assertEquals(outCome.getXY(1, 3), 2);
    assertEquals(outCome.getXY(2, 4), 4);
    assertEquals(outCome.getXY(3, 5), 6);
    assertEquals(outCome.getXY(4, 6), 8);
  }

  private static final void assertArrayDeepEquals(int[][] actual,
      int[][] expected) {
    assertEquals(actual.length, expected.length);
    for (int i = 0; i < actual.length; i++) {
      assertArrayEquals(actual[i], expected[i]);
    }
  }

  @Test
  public void getGridTest() {
    /* This is a test to make sure that getGrid returns the
    correct grid. We start the test by creating two boards, and
    setting up two board variables for later use. Then we use a
    for loop to create rows, columns and values and set them to
    the boards we created. Then we check to make sure that the grid
    given by the board is the same as the output of setRowColumn that was
    used to give that board values.
    */

    /*We start the test by creating two boards, and
    setting up two board variables for later use.
    */
    Board one = new Board();
    Board two = new Board();
    Board three = new Board();
    Board output1 = new Board();
    Board output2 = new Board();
    Board output3 = new Board();
    int[][] onere = output1.getGrid();
    int[][] twore = output2.getGrid();
    int[][] threere = output3.getGrid();
    
    

    /*Then we use a
    for loop to create rows, columns and values and set them to
    the boards we created.
    */
    
    
    for (int i = 0; i < 6; i++) {
      int row = i;
      int column = i + 2;
      int value = i * 2;
      onere[row][column] = value;
      one.setRowColumn(row, column, value);
    }
  // int[][] output1 = one.getGrid();
    for (int i = 0; i < 4; i++) {
      int row = i;
      int column = i + 6;
      int value = i * 4;
    twore[row][column] = value;
      two.setRowColumn(row, column, value);
    }
  // int[][] output2 = two.getGrid();
    for (int i = 0; i < 6; i++) {
      int row = i + 3;
      int column = i + 1;
      int value = i * 7;
    threere[row][column] = value;
      three.setRowColumn(row, column, value);
    }
  // int[][] output3 = three.getGrid();
    /*Then we check to make sure that the grid
    given by the board is the same as the output of setRowColumn that was
    used to give that board values.
    */
    
    
    assertArrayDeepEquals(onere, one.getGrid());
    assertArrayDeepEquals(twore, two.getGrid());
    assertArrayDeepEquals(threere, three.getGrid());
  }


  @Test
  public void sizeTest() {
    /*This is a test to make sure that size()
    return the correct size of the board.*/
    Board one = new Board(14);
    Board two = new Board(144);
    Board three = new Board(568);
    Board four = new Board(0);

    assertEquals(one.size(), 14);
    assertEquals(two.size(), 144);
    assertEquals(three.size(), 568);
    assertEquals(four.size(), 0);

  }












}
