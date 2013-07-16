package serverize;

public class Watchdog extends Thread {

    private static final long SLEEP_INTERVAL = Configuration.loadLong("SLEEP_INTERVAL_MS", 10 * 1000); // 10 seconds
    private static final long SESSION_TIMEOUT = Configuration.loadLong("SESSION_TIMEOUT_MS", 10 * 1000); // 10 seconds

    private String sessionId;
    private long lastHeartbeatTime;

    public Watchdog(String sessionId) {
        this.sessionId = sessionId;
        heartbeat();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(SLEEP_INTERVAL);
                if (isSessionDead()) {
                    System.out.println("a session is dead: " + sessionId);
                    InteractiveSessionManager.getInstance().killSession(sessionId);
                    break;
                }
            }
        }
        catch (InterruptedException e) {
            // exit
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
