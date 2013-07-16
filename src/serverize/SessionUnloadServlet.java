package serverize;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUnloadServlet extends HttpServlet {

    private static final long serialVersionUID = -2746861299745565939L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getParameter("sessionId");
        if (sessionId == null) return;
        InteractiveSessionManager.getInstance().killSession(sessionId);
    }
}
