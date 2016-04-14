package edu.brown.cs.blokus;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import edu.brown.cs.blokus.Player;


public class PlayerTest {


  @Test
  public void hasPieceTest() {
    /*This is a test to make sure that the hasPiece function
      works properly. We first create two new players. Then
      we loop through all of their pieces and call hasPiece. We
      store the results of those calls in a boolean list. This
      is done for both players. Then, we loop through the boolean
      list to make sure that all of the values are "true." This would
      mean that a player has all pieces, which is correct.
    */

    /*We first create two new players.*/
    Player player1 = new Player("one");
    Player player2 = new Player("two");

    /*Then
    we loop through all of their pieces and call hasPiece. We
    store the results of those calls in a boolean list. This
    is done for both players.*/
    ArrayList<Boolean> bools1 = new ArrayList<Boolean>();
    //int i = 0;
    for (Shape s : Shape.values()) {
      bools1.add(player1.hasPiece(s));
     // i++;
    }
    ArrayList<Boolean> bools2 = new ArrayList<Boolean>();
    //int i = 0;
    for (Shape s : Shape.values()) {
      bools2.add(player2.hasPiece(s));
     // i++;
    }

    Boolean oneHasAllPeaces = true;
    Boolean twoHasAllPeaces = true;
    /*Then, we loop through the boolean
    list to make sure that all of the values are "true." This would
    mean that a player has all pieces, which is correct.
    */
    for (int n = 0; n < bools1.size(); n++) {
      if (bools1.get(n) == false) {
        oneHasAllPeaces = false;
        break;
      }
    }

    for (int x = 0; x < bools2.size(); x++) {
      if (bools2.get(x) == false) {
        twoHasAllPeaces = false;
        break;
      }
    }
    assert(oneHasAllPeaces);
    assert(twoHasAllPeaces);
  }



  @Test
  public void removePieceTest() {
    /* This test is for removePieceTest(). We start by creating
       two new players. Then we create a list of shapes and add
       in seven different shapes. We then remove the seven different
       shapes from each player. A list of booleans is created for each
       player. Then, we loop through the list of shapes and use hasPiece()
       to check if the player has that piece. The opposite of the result of
       hashPiece() is stored in the list of booleans. Then we go through
       the list of booleans to make sure that they are true, which wouuld
       mean that all of the pieces that were intented to me removed are actually
       removed.
    */

      /*We start by creating
       two new players.*/
      Player player1 = new Player("one");
      Player player2 = new Player("two");

        /*Then we create a list of shapes and add
        in seven different shapes.
        */
        List<Shape> shapes = new ArrayList<Shape>();
        shapes.add(Shape.V3);
        shapes.add(Shape.I3);
        shapes.add(Shape.L4);
        shapes.add(Shape.T4);
        shapes.add(Shape.F5);
        shapes.add(Shape.L5);
        shapes.add(Shape.V5);

        /*We then remove the seven different
        shapes from each player.*/
        player2.removePiece(Shape.V3);
        player2.removePiece(Shape.I3);
        player2.removePiece(Shape.L4);
        player2.removePiece(Shape.T4);
        player2.removePiece(Shape.F5);
        player2.removePiece(Shape.L5);
        player2.removePiece(Shape.V5);
        player1.removePiece(Shape.V3);
        player1.removePiece(Shape.I3);
        player1.removePiece(Shape.L4);
        player1.removePiece(Shape.T4);
        player1.removePiece(Shape.F5);
        player1.removePiece(Shape.L5);
        player1.removePiece(Shape.V5);

        /*A list of booleans is created for each
        player.*/
        List<Boolean> results1 = new ArrayList<Boolean>();
        List<Boolean> results2 = new ArrayList<Boolean>();

        /*Then, we loop through the list of shapes and use hasPiece()
        to check if the player has that piece. The opposite of the result of
        hashPiece() is stored in the list of booleans.
        */
        for (int i = 0; i < shapes.size(); i++) {
          results1.add(!player1.hasPiece(shapes.get(i)));
        }
        for (int i = 0; i< shapes.size(); i++) {
          results2.add(!player2.hasPiece(shapes.get(i)));
        }

        Boolean removedPieces1 = true;
        Boolean removedPieces2 = true;
        /*Then we go through
        the list of booleans to make sure that they are true, which wouuld
        mean that all of the pieces that were intented to me removed are actually
        removed.
        */
        for (int i = 0; i < results1.size(); i++) {
          if (results1.get(i) == false) {
            removedPieces1 = false;
            break;
          }
        }
        for (int i = 0; i < results2.size(); i++) {
          if (results2.get(i) == false) {
            removedPieces2 = false;
            break;
          }
        }
        assert(removedPieces1);
        assert(removedPieces2);
  }


@Test
public void getAddScoreTest() {
	/* This is a test to make sure that addScore and setScore
    work properly. We first create two players, then add setScore
    and check if the score if what it should be.
 */
	
	Player player1 = new Player("one");
	Player player2 = new Player("two");
	
	player1.addScore(3);
    assertEquals(player1.getScore(), 3);
    player1.addScore(6);
    assertEquals(player1.getScore(), 9);
    player1.addScore(0);
    assertEquals(player1.getScore(), 9);

    player2.addScore(0);
    assertEquals(player2.getScore(), 0);
    player2.addScore(1);
    assertEquals(player2.getScore(), 1);
    player2.addScore(100);
    assertEquals(player2.getScore(), 101);
}





@Test
public void playingStatusTest() {
  /*This is a test for checking to see is a player is
    currently playing or not. We create a player. At this point
    the status of the player should be "playing." Then we call
    stopPlaying(), which should change the status of playing to false.
  */
  Player player = new Player("one");
  assertEquals(player.isPlaying(), true);
  player.stopPlaying();
  assertEquals(!player.isPlaying(), true);

  Player player2 = new Player("two");
  assertEquals(player2.isPlaying(), true);
  player2.stopPlaying();
  assertEquals(!player2.isPlaying(), true);


}


@Test
public void remainingPiecesTest() {
  /*This is a test for the getRemainingPieces() function. We
    first create two players. Then we create a list of shapes
    that are going to be removed from the player's shapes. Those
    shapes are removed by calling the removePiece() function. We then
    loop through the list of shapes to make sure that they are not
    contained in the remaining pieces, in which case the test passes. */

  /*We first create two players.*/
  Player player = new Player("one");
  Player player1 = new Player("two");

  /*Then we create a list of shapes
  that are going to be removed from the player's shapes.*/
  List<Shape> shapes = new ArrayList<Shape>();
  shapes.add(Shape.V3);
  shapes.add(Shape.L4);
  shapes.add(Shape.V5);
  List<Shape> shapes1 = new ArrayList<Shape>();
  shapes1.add(Shape.V3);
  shapes1.add(Shape.L4);
  shapes1.add(Shape.V5);
  shapes1.add(Shape.I3);
  shapes1.add(Shape.T4);
  shapes1.add(Shape.F5);
  shapes1.add(Shape.L5);

  /*Those shapes are removed by calling the removePiece() function. */
  player.removePiece(Shape.V3);
  player.removePiece(Shape.L4);
  player.removePiece(Shape.V5);

  for (int i = 0; i < shapes1.size(); i++) {
    player1.removePiece(shapes1.get(i));
  }


  /*We then loop through the list of shapes to make sure that they are not
  contained in the remaining pieces, in which case the test passes. */
  Boolean notThere = true;
  for (int i = 0; i < shapes.size(); i++) {
    if (player.getRemainingPieces().contains(shapes.get(i))){
      notThere = false;
      break;
    }
  }
  assert(notThere);

  Boolean notThereEither = true;
  for (int i = 0; i < shapes1.size(); i++) {
    if (player1.getRemainingPieces().contains(shapes1.get(i))){
      notThereEither = false;
      break;
    }
  }
  assert(notThereEither);

}














}
