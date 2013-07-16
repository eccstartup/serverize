package serverize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReaderThread extends Thread {

    static class Pair {
        byte[] b;
        int c;

        Pair(byte[] b, int c) {
            this.b = b;
            this.c = c;
        }
    }

    private static final int READER_THREAD_BUFFER_SIZE = Configuration.loadInt("READER_THREAD_BUFFER_SIZE", 1024);

    private InputStream in;
    private BlockingQueue<Pair> buffer;

    public ReaderThread(InputStream in) {
        this.in = in;
        this.buffer = new LinkedBlockingQueue<Pair>();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                byte[] b = new byte[READER_THREAD_BUFFER_SIZE];
                int count = in.read(b);
                if (count == -1) break;
                buffer.put(new Pair(b, count));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            // exit
        }
    }

    public String take() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            while (true) {
                Pair pair = buffer.remove();
                ps.write(pair.b, 0, pair.c);
            }
        }
        catch (NoSuchElementException e) {
            // ignore
        }
        return baos.toString();
    }
}
