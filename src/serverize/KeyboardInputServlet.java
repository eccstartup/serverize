package serverize;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KeyboardInputServlet extends HttpServlet {

    private static final long serialVersionUID = -6870619732045455226L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getParameter("sessionId");
        if (sessionId == null) return;

        String input = request.getParameter("input");
        if (input == null) return;

        InteractiveSession session = InteractiveSessionManager.getInstance().getSession(sessionId);
        if (session == null) return;

        session.feedKeyboardInput(input);

        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("ok");
    }
}
