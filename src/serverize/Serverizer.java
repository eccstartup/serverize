package serverize;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Serverizer {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new HomeServlet(args[0])), "/");
        context.addServlet(new ServletHolder(new KeyboardInputServlet()), "/input");
        context.addServlet(new ServletHolder(new ScreenOutputServlet()), "/output");
        context.addServlet(new ServletHolder(new ScriptServlet()), "/script.js");

        Server server = new Server(9999);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
