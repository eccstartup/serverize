package serverize;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class InteractiveSessionManager {

    private static final int MAX_SESSIONS = Configuration.loadInt("MAX_SESSIONS", 5);
    private static final int SESSION_WAIT_SECONDS = Configuration.loadInt("SESSION_WAIT_SECONDS", 10);

    private static InteractiveSessionManager instance = new InteractiveSessionManager();

    public static InteractiveSessionManager getInstance() {
        return instance;
    }

    private Semaphore availableSessions;
    private Map<String, InteractiveSession> sessions;
    private Map<String, Watchdog> watchdogs;
    private Map<String, Thread> shutdownHooks;

    private InteractiveSessionManager() {
        availableSessions = new Semaphore(MAX_SESSIONS, true);
        sessions = new ConcurrentHashMap<String, InteractiveSession>();
        watchdogs = new ConcurrentHashMap<String, Watchdog>();
        shutdownHooks = new ConcurrentHashMap<String, Thread>();
    }

    public String newSession(String program) throws InterruptedException, IOException {
        if (!availableSessions.tryAcquire(SESSION_WAIT_SECONDS, TimeUnit.SECONDS)) {
            throw new RuntimeException("User limit exceeded: MAX_SESSIONS=" + MAX_SESSIONS);
        }

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
            availableSessions.release();
        }
        if (watchdogs.containsKey(sessionId)) {
            watchdogs.remove(sessionId).interrupt();
        }
        if (shutdownHooks.containsKey(sessionId)) {
            Runtime.getRuntime().removeShutdownHook(shutdownHooks.remove(sessionId));
        }
    }
}
