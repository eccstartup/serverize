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
    private Map<String, Thread> shutdownHooks;

    private InteractiveSessionManager() {
        sessions = new ConcurrentHashMap<String, InteractiveSession>();
        watchdogs = new ConcurrentHashMap<String, Watchdog>();
        shutdownHooks = new ConcurrentHashMap<String, Thread>();
    }

    public String newSession(String program) throws InterruptedException {
        try {
            InteractiveSession session = new InteractiveSession(program);
            String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, session);

            Watchdog watchdog = new Watchdog(sessionId);
            watchdogs.put(sessionId, watchdog);
            watchdog.start();

            Thread shutdownHook = new ShutdownHook(sessionId);
            shutdownHooks.put(sessionId, shutdownHook);
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            return sessionId;
        }
        catch (IOException e) {
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

    public void killSession(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            sessions.remove(sessionId).destroy();
        }
        if (watchdogs.containsKey(sessionId)) {
            watchdogs.remove(sessionId).interrupt();
        }
        if (shutdownHooks.containsKey(sessionId)) {
            Runtime.getRuntime().removeShutdownHook(shutdownHooks.remove(sessionId));
        }
    }
}
