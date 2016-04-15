package edu.brown.cs.blokus;

import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.GameSettings.Builder;
import edu.brown.cs.blokus.GameSettings.Type;
import edu.brown.cs.blokus.Player;

import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GameSettingsTest {

	@Test
	public void builderTest() {

		Builder one = new Builder("one");
		one.type(Type.PUBLIC);
		//one.state(PLAYING);
		one.maxPlayers(4);
		one.timer(1);
		GameSettings setting = one
      .player(Turn.FIRST, new Player(new ObjectId().toString()))
      .build();

		assertEquals(setting.getId(), "one");
		assertEquals(setting.getType(), Type.PUBLIC);
		//assertEquals(setting.getState(), PLAYING);
		assertEquals(setting.getMaxPlayers(), 4);
		assertEquals(setting.getTimer(), 1);
		assertEquals(setting.hasId(), true);

		Builder two = new Builder();
		two.type(Type.PRIVATE);
		//two.state(UNSTARTED);
		two.maxPlayers(2);
		two.timer(0);
		GameSettings settings = two
      .player(Turn.FIRST, new Player(new ObjectId().toString()))
      .build();

		assertEquals(settings.getType(), Type.PRIVATE);
		//assertEquals(setting.getState(), UNSTARTED);
		assertEquals(settings.getMaxPlayers(), 2);
		assertEquals(settings.getTimer(), 0);
		assertEquals(settings.hasId(), false);
	}




}
