package serverize;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScreenOutputServlet extends HttpServlet {

    private static final long serialVersionUID = 7984971923327865847L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getParameter("sessionId");
        if (sessionId == null) return;

        InteractiveSession session = InteractiveSessionManager.getInstance().getSession(sessionId);
        if (session == null) return;

        String outputs = session.drainScreenOutputs();

        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(outputs);
    }
}
