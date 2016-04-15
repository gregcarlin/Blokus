package edu.brown.cs.blokus.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.Shape;
import edu.brown.cs.blokus.Turn;

import org.bson.types.ObjectId;
import org.junit.AfterClass;
import org.junit.Before;
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
  }

  @Before
  public void emptyDb() {
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

  private static GameSettings.Builder randomGameSettings() {
    return new GameSettings.Builder()
      .type(GameSettings.Type.PUBLIC)
      .state(GameSettings.State.PLAYING)
      .maxPlayers(Math.random() < 0.5 ? 2 : 4)
      .player(Turn.FIRST, randomPlayer())
      .player(Turn.SECOND, randomPlayer())
      .player(Turn.THIRD, randomPlayer())
      .player(Turn.FOURTH, randomPlayer())
      .timer((int) (Math.random() * 100000));
  }

  private static List<Shape> randomShapes() {
    List<Shape> all = Arrays.asList(Shape.values());
    Collections.shuffle(all);
    return all.subList(0, (int) (Math.random() * all.size()));
  }

  private static Player randomPlayer() {
    return new Player(new ObjectId().toString(), randomShapes(),
        (int) (Math.random() * 84), Math.random() < 0.5);
  }

  private static Game.Builder randomGame() {
    int[][] grid = new int[20][20];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = (int) (Math.random() * 4);
      }
    }

    return new Game.Builder()
      .setGrid(grid)
      .setTurn(Turn.values()[(int) (Math.random() * Turn.values().length)])
      .setLastTurnTime(System.currentTimeMillis())
      .setSettings(randomGameSettings().build());
  }

  private static void assertGameSettingsEquals(GameSettings expected,
      GameSettings actual) {
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getMaxPlayers(), actual.getMaxPlayers());
    assertEquals(expected.getTimer(), actual.getTimer());
  }

  private static void assertGameEquals(Game expected, Game actual) {
    assertNotNull(actual);

    int[][] grid = expected.getGrid();
    int[][] retrievedGrid = actual.getGrid();
    for (int i = 0; i < retrievedGrid.length; i++) {
      assertArrayEquals(grid[i], retrievedGrid[i]);
    }

    for (Turn turn : Turn.values()) {
      assertEquals(expected.getPlayer(turn), actual.getPlayer(turn));
    }

    assertEquals(expected.getTurn(), actual.getTurn());
    assertEquals(expected.getLastTurnTime(), actual.getLastTurnTime());

    assertGameSettingsEquals(expected.getSettings(), actual.getSettings());
  }

  @Test
  public void gameNotFound() {
    assertNull(db.getGame(new ObjectId().toString()));
  }

  @Test
  public void saveAndLoadGame() {
    Game game = randomGame().build();
    String id = db.saveGame(game);

    Game retrieved = db.getGame(id);
    assertEquals(id, retrieved.getSettings().getId());
    assertGameEquals(game, retrieved);
  }

  @Test
  public void noOpenGames() {
    assertEquals(Collections.emptyList(),
        db.getOpenGames(0, new ObjectId().toString()));
  }

  @Test
  public void openGames() {
    Game gameA = randomGame()
      .setSettings(randomGameSettings()
          .type(GameSettings.Type.PUBLIC)
          .state(GameSettings.State.UNSTARTED)
          .build())
      .build();
    Game gameB = randomGame()
      .setSettings(randomGameSettings()
          .type(GameSettings.Type.PUBLIC)
          .state(GameSettings.State.UNSTARTED)
          .build())
      .build();
    String idA = db.saveGame(gameA);
    String idB = db.saveGame(gameB);

    List<GameSettings> open = db.getOpenGames(0, new ObjectId().toString());
    assertEquals(2, open.size());
    assertGameSettingsEquals(gameA.getSettings(), open.get(0));
    assertEquals(idA, open.get(0).getId());
    assertGameSettingsEquals(gameB.getSettings(), open.get(1));
    assertEquals(idB, open.get(1).getId());
  }

  @Test
  public void joinedGames() {
    Player playerA = randomPlayer();
    Game gameA = randomGame()
      .setSettings(randomGameSettings()
          .state(GameSettings.State.PLAYING)
          .player(Turn.FIRST, playerA)
          .build())
      .build();
    String idA = db.saveGame(gameA);
    Player playerB = randomPlayer();
    Game gameB = randomGame()
      .setSettings(randomGameSettings()
          .state(GameSettings.State.UNSTARTED)
          .player(Turn.SECOND, playerB)
          .player(Turn.FOURTH, playerA)
          .build())
      .build();
    String idB = db.saveGame(gameB);

    List<GameSettings> withB = db.getGamesWith(playerB.getId());
    assertEquals(1, withB.size());
    assertGameSettingsEquals(gameB.getSettings(), withB.get(0));

    List<GameSettings> withA = db.getGamesWith(playerA.getId());
    assertEquals(2, withA.size());
    assertGameSettingsEquals(gameA.getSettings(), withA.get(0));
    assertGameSettingsEquals(gameB.getSettings(), withA.get(1));
  }

  /* see me */ @AfterClass
  public static void closeDb() {
    if (db != null) {
      db.close();
    }
  }
}
