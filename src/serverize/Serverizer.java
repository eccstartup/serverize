package serverize;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Serverizer {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage:   [port] [program]");
            System.err.println("Example:  9999  /usr/local/bin/irb");
            System.exit(1);
        }

        int port = Integer.valueOf(args[0]);
        String program = args[1];

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new HomeServlet(program)), "/");
        context.addServlet(new ServletHolder(new KeyboardInputServlet()), "/input");
        context.addServlet(new ServletHolder(new ScreenOutputServlet()), "/output");
        context.addServlet(new ServletHolder(new SessionUnloadServlet()), "/unload");
        context.addServlet(new ServletHolder(new ScriptServlet()), "/script.js");

        Server server = new Server(port);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
