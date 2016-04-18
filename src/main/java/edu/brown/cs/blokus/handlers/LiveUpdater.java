package edu.brown.cs.blokus.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.GameSettings;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.Player;
import edu.brown.cs.blokus.db.Database;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class LiveUpdater {
  private static final Gson GSON = new Gson();
  private static final Set<Session> unknownSessions = new HashSet<>();
  // maps userId to session
  private static final Map<String, Session> knownSessions = new HashMap<>();

  @OnWebSocketConnect
  public void connected(Session session) {
    unknownSessions.add(session);
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    unknownSessions.remove(session);
    knownSessions.values().remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) {
    if (Database.sessions.containsKey(message)) {
      System.out.println("recognized " + Database.sessions.get(message));
      knownSessions.put(Database.sessions.get(message), session);
    }
  }

  // should be called when a move is made
  public static void moveMade(Game context, Move move) {
    for (Player player : context.getAllPlayers()) {
      String playerId = player.getId();
      if (knownSessions.containsKey(playerId)) {
        Session session = knownSessions.get(playerId);
        JsonObject jObj = new JsonObject();
        jObj.addProperty("piece", move.getShape().ordinal());
        jObj.addProperty("orientation", move.getOrientation().ordinal());
        jObj.addProperty("x", move.getX());
        jObj.addProperty("y", move.getY());
        boolean playing
          = context.getSettings().getState() == GameSettings.State.PLAYING;
        jObj.addProperty("next_player",
            playing ? context.getTurn().ordinal() : -1);
        try {
          session.getRemote().sendString(GSON.toJson(jObj));
        } catch (IOException e) {
          System.err.println(e);
        }
      }
    }
  }

  // should be called when game state changes
  public static void stateChanged(GameSettings context) {
    // TODO update relevant users
  }
}
