package serverize;

public class ShutdownHook extends Thread {

    private String sessionId;

    public ShutdownHook(String sessionId) {
        this.sessionId = sessionId;
    }

    public void run() {
        InteractiveSessionManager.getInstance().killSession(sessionId);
    }
}
