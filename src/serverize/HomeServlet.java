package serverize;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = -1547324779185515235L;

    private String program;

    public HomeServlet(String program) {
        this.program = program;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("request URI: " + request.getRequestURI());
        if (!request.getRequestURI().equals("/")) return;

        String sessionId = InteractiveSessionManager.getInstance().newSession(program);
        if (sessionId == null) return;

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset=\"utf-8\">"
            + "<title>" + program + "</title>"
            + "<style>"
            + "* { font-family: monospace; font-size: 20px; margin: 0; padding: 0; }"
            + "html, body { width: 100%; height: 100%; }"
            + "#inputbox { position: absolute; top: 0; left: 0; right: 0; height: 30px; width: 100%; }"
            + "#console { position: absolute; top: 30px; left: 0; right: 0; bottom: 0; width: 100%; }"
            + "</style>"
            + "<script src=\"http://code.jquery.com/jquery-1.10.2.min.js\"></script>"
            + "<script>window.sessionId = '" + sessionId + "';</script>"
            + "<script src=\"script.js\"></script>"
            + "</head>"
            + "<body>"
            + "<input type=\"text\" id=\"inputbox\">"
            + "<textarea id=\"console\"></textarea>"
            + "</body>"
            + "</html>");
    }
}
