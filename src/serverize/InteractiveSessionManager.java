package serverize;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InteractiveSessionManager {

    private static InteractiveSessionManager instance = new InteractiveSessionManager();

    public static InteractiveSessionManager getInstance() {
        return instance;
    }

    private Map<String, InteractiveSession> sessions;
    private Map<String, Watchdog> watchdogs;

    private InteractiveSessionManager() {
        sessions = new ConcurrentHashMap<String, InteractiveSession>();
        watchdogs = new ConcurrentHashMap<String, Watchdog>();
    }

    public String newSession(String program) {
        try {
            final InteractiveSession session = new InteractiveSession(program);
            final String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, session);

            Watchdog watchdog = new Watchdog(sessionId, session, sessions);
            watchdogs.put(sessionId, watchdog);
            watchdog.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("runtime is shutting down... destroy session " + sessionId);
                    session.destroy();
                }
            });

            return sessionId;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InteractiveSession getSession(String sessionId) {
        Watchdog watchdog = watchdogs.get(sessionId);
        if (watchdog != null) {
            watchdog.heartbeat();
        }
        return sessions.get(sessionId);
    }
}
