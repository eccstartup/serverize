package serverize;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScriptServlet extends HttpServlet {

    private static final long serialVersionUID = -5341261916413501208L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(getScriptContent());
    }

    private static String getScriptContent() {
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(ScriptServlet.class.getResourceAsStream("script.js"));
        while (in.hasNextLine()) {
            sb.append(in.nextLine());
            sb.append('\n');
        }
        in.close();
        return sb.toString();
    }
}
