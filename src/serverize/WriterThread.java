package serverize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WriterThread extends Thread {

    private OutputStream out;
    private BlockingQueue<String> buffer;

    public WriterThread(OutputStream out) {
        this.out = out;
        this.buffer = new LinkedBlockingQueue<String>();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String s = buffer.take();
                if (s.isEmpty()) continue;
                String line = s + "\n";
                out.write(line.getBytes());
                out.flush();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            // exit
        }
    }

    public void put(String s) throws InterruptedException {
        buffer.put(s);
    }
}
