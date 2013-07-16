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

    private InteractiveSessionManager() {
        sessions = new ConcurrentHashMap<String, InteractiveSession>();
    }

    public String newSession(String program) {
        try {
            InteractiveSession session = new InteractiveSession(program);
            // TODO timeout thread

            String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, session);
            return sessionId;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InteractiveSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
