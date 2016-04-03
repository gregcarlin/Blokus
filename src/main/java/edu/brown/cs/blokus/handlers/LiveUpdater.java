package edu.brown.cs.blokus.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.blokus.Game;
import edu.brown.cs.blokus.Move;
import edu.brown.cs.blokus.db.Database;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class LiveUpdater {
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
    // TODO update relevant users
  }
}
