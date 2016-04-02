package edu.brown.cs.blokus.db;

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

  // TODO tests for game settings

  @AfterClass
  public static void closeDb() {
    if (db != null) {
      db.close();
    }
  }
}
