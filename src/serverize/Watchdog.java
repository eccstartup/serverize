package serverize;

import java.util.Map;

public class Watchdog extends Thread {

    private static final long SLEEP_INTERVAL = 10 * 1000; // 10 seconds
    private static final long SESSION_TIMEOUT = 10 * 1000; // 10 seconds

    private String sessionId;
    private InteractiveSession session;
    private Map<String, InteractiveSession> sessions;
    private long lastHeartbeatTime;

    public Watchdog(String sessionId, InteractiveSession session, Map<String, InteractiveSession> sessions) {
        this.sessionId = sessionId;
        this.session = session;
        this.sessions = sessions;
        heartbeat();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
                if (isSessionDead()) {
                    System.out.println("a session is dead: " + sessionId);
                    sessions.remove(sessionId);
                    session.destroy();
                    break;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSessionDead() {
        return lastHeartbeatTime + SESSION_TIMEOUT < System.currentTimeMillis();
    }

    public void heartbeat() {
        System.out.println("heartbeat " + sessionId);
        lastHeartbeatTime = System.currentTimeMillis();
    }
}
