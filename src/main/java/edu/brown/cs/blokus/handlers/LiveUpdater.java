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


/**
  * The websocket handler that updates clients live.
  */
@WebSocket
public class LiveUpdater {
  private static final Gson GSON = new Gson();
  private static final Set<Session> UNKNOWN_SESSIONS = new HashSet<>();
  // maps userId to session
  private static final Map<String, Session> KNOWN_SESSIONS = new HashMap<>();

  /**
    * Should be called when connection to a new session.
    * @param session the newly connected session
    */
  @OnWebSocketConnect
  public void connected(Session session) {
    UNKNOWN_SESSIONS.add(session);
  }

  /**
    * Should be called when a session is closed.
    * @param session the closed session
    * @param statusCode the status of the session
    * @param reason the reason the session was closed
    */
  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    UNKNOWN_SESSIONS.remove(session);
    KNOWN_SESSIONS.values().remove(session);
  }

  /**
    * Should be called when a message is received from a session.
    * @param session the session
    * @param message the message
    */
  @OnWebSocketMessage
  public void message(Session session, String message) {
    if (Database.SESSIONS.containsKey(message)) {
      System.out.println("recognized " + Database.SESSIONS.get(message));
      KNOWN_SESSIONS.put(Database.SESSIONS.get(message), session);
    }
  }

  /**
    * Should be called when a move is made.
    * @param context the game the move was made in
    * @param move the move made
    */
  public static void moveMade(Game context, Move move) {
    for (Player player : context.getAllPlayers()) {
      String playerId = player.getId();
      if (KNOWN_SESSIONS.containsKey(playerId)) {
        Session session = KNOWN_SESSIONS.get(playerId);
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

  /**
    * Should be called when the state of a game changes.
    * @param context the settings of the game whose state changed
    */
  public static void stateChanged(GameSettings context) {
    // TODO update relevant users
  }
}
