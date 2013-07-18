package serverize;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticFilesServlet extends HttpServlet {

    private static final long serialVersionUID = -5341261916413501208L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().endsWith(".css")) {
            response.setContentType("text/css");
        }
        else if (request.getRequestURI().endsWith(".js")) {
            response.setContentType("text/javascript");
        }
        else if (request.getRequestURI().endsWith(".json")) {
            response.setContentType("application/json");
        }
        else {
            response.setContentType("text/plain");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(getContent(request.getRequestURI().substring(1)));
    }

    private static String getContent(String filename) {
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(StaticFilesServlet.class.getResourceAsStream(filename));
        while (in.hasNextLine()) {
            sb.append(in.nextLine());
            sb.append('\n');
        }
        in.close();
        return sb.toString();
    }
}
