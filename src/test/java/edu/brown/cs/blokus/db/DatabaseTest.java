package edu.brown.cs.blokus.db;

import java.util.Arrays;
import java.util.Collections;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.Turn;

import org.bson.types.ObjectId;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
  * Unit tests for the blokus database connection layer.
  */
public class DatabaseTest {
  private static Database db;

  @BeforeClass
  public static void setupDb() {
    db = new Database("blokus-test");
    db.empty();
  }

  @Test
  public void signUpAndLogIn() {
    String id = db.createUser("test-user", "test-pass");
    assertNotNull(id);
    assertTrue(id.length() > 0);
    assertEquals(id, db.getUserId("test-user", "test-pass"));

    assertNull(db.getUserId("test-user", "wrong-pass"));

    String hash = db.logIn(id);
    assertNotNull(hash);
    assertTrue(hash.length() > 0);
    assertEquals(id, db.getUserId(hash));
  }

  @Test
  public void noUser() {
    assertNull(db.getUserId("not-found", "no-pass"));
  }

  @Test
  public void game() {
    int[][] grid = new int[20][20];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = (int) (Math.random() * 4);
      }
    }

    Player red = new Player(new ObjectId().toString(),
        Arrays.asList(Shape.ONE, Shape.I3), 7, true);
    Player blue = new Player(new ObjectId().toString(),
        Arrays.asList(Shape.TWO, Shape.I3, Shape.T4), 9, true);
    Player green = new Player(new ObjectId().toString(),
        Collections.emptySet(), 21, false);
    Player yellow = new Player(new ObjectId().toString(),
        Arrays.asList(Shape.values()), 0, true);

    GameSettings settings = new GameSettings.Builder()
      .type(GameSettings.Type.PUBLIC)
      .maxPlayers(4)
      .timer(1000)
      .build();

    Game game = new Game.Builder()
      .setGrid(grid)
      .setPlayer(Turn.FIRST, red)
      .setPlayer(Turn.SECOND, blue)
      .setPlayer(Turn.THIRD, green)
      .setPlayer(Turn.FOURTH, yellow)
      .setTurn(Turn.SECOND)
      .setLastTurnTime(System.currentTimeMillis())
      .setSettings(settings)
      .build();

    String id = db.saveGame(game);

    Game retrieved = db.getGame(id);
    assertNotNull(retrieved);

    int[][] retrievedGrid = retrieved.getGrid();
    for (int i = 0; i < retrievedGrid.length; i++) {
      assertArrayEquals(grid[i], retrievedGrid[i]);
    }

    assertEquals(retrieved.getPlayer(Turn.FIRST), red);
    assertEquals(retrieved.getPlayer(Turn.SECOND), blue);
    assertEquals(retrieved.getPlayer(Turn.THIRD), green);
    assertEquals(retrieved.getPlayer(Turn.FOURTH), yellow);

    assertEquals(Turn.SECOND, retrieved.getTurn());
    assertEquals(game.getLastTurnTime(), retrieved.getLastTurnTime());

    GameSettings retrievedSettings = retrieved.getSettings();
    assertEquals(id, retrievedSettings.getId());
    assertEquals(GameSettings.Type.PUBLIC, retrievedSettings.getType());
    assertEquals(4, retrievedSettings.getMaxPlayers());
    assertEquals(1000, retrievedSettings.getTimer());
  }

  @AfterClass
  public static void closeDb() {
    if (db != null) {
      db.close();
    }
  }
}
