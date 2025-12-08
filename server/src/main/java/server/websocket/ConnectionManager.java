package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import websocket.messages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void join(Integer gameID, Session session) {
        connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    public void leave(Integer gameID, Session session) {
        Set<Session> set = connections.get(gameID);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void sendToClient(Session session, ServerMessage notification) throws IOException {
        String msg = notification.toString();

        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }

    public void broadcast(Session excludeSession, int gameID, ServerMessage notification) throws IOException {
        Set<Session> set = connections.get(gameID);

        String msg = notification.toString();
        for (Session c : set) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
