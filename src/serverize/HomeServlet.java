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

        try {
            String sessionId = InteractiveSessionManager.getInstance().newSession(program);

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"utf-8\">"
                + "<title>" + program + "</title>"
                + "<link rel=\"stylesheet\" href=\"style.css\">"
                + "<script src=\"http://code.jquery.com/jquery-1.10.2.min.js\"></script>"
                + "<script src=\"jwerty.js\"></script>"
                + "</head>"
                + "<body>"
                + "<div id=\"console\"></div>"
                + "<script>window.sessionId = '" + sessionId + "';</script>"
                + "<script src=\"script.js\"></script>"
                + "</body>"
                + "</html>");
        }
        catch (InterruptedException e) {
            // ignore
        }
    }
}
