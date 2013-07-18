package serverize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InteractiveSession {

    private static final String SCRIPT_UTILITY = Configuration.loadString("SCRIPT_UTILITY_PATH", "/usr/bin/script");

    private Process process;
    private ReaderThread reader;
    private WriterThread writer;

    public InteractiveSession(String program) throws IOException, InterruptedException {
        process = new ProcessBuilder(SCRIPT_UTILITY, "-q", "/dev/null", program).start();
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
        if (process != null) {
            process.destroy();
            process = null;
        }
        if (reader != null) {
            reader.interrupt();
            reader = null;
        }
        if (writer != null) {
            writer.interrupt();
            writer = null;
        }
    }

    protected void finalize() {
        destroy();
    }
}
