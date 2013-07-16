package serverize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InteractiveSession {

    private Process process;
    private ReaderThread reader;
    private WriterThread writer;

    public InteractiveSession(String program) throws IOException, InterruptedException {
        process = new ProcessBuilder("/usr/bin/script", "-q", "/dev/null", program).start();
        OutputStream stdin = process.getOutputStream();
        InputStream stdout = process.getInputStream();
        InputStream stderr = process.getErrorStream();

        reader = new ReaderThread(stdout);
        writer = new WriterThread(stdin);
        reader.start();
        writer.start();
    }

    public String drainScreenOutputs() {
        return reader.take();
    }

    public void feedKeyboardInput(String input) throws InterruptedException {
        writer.put(input);
    }

    public synchronized void destroy() {
        if (process == null) return;

        process.destroy();
        process = null;
        System.out.println("a program is shutdown");

        reader.interrupt();
        writer.interrupt();
    }

    protected void finalize() {
        destroy();
    }
}
