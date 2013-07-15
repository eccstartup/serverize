import java.io.*;
import java.util.*;

public class Serverizer {
    
    static class ReaderThread extends Thread {
        InputStream in;
        ReaderThread(InputStream in) {
            this.in = in;
        }
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1) break;
                    System.out.write(buffer, 0, count);
                }
            }
            catch (IOException e) {
                // ignore
            }
        }
    }
    
    static class WriterThread extends Thread {
        OutputStream out;
        WriterThread(OutputStream out) {
            this.out = out;
        }
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine() + "\n";
                    out.write(line.getBytes());
                    out.flush();
                }
            }
            catch (IOException e) {
                // ignore
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        Process p = new ProcessBuilder("/usr/bin/script", "-q", "/dev/null", args[0]).start();
        OutputStream stdin = p.getOutputStream();
        InputStream stdout = p.getInputStream();
        InputStream stderr = p.getErrorStream();
        ReaderThread reader = new ReaderThread(stdout);
        WriterThread writer = new WriterThread(stdin);
        reader.start();
        writer.start();
        reader.join();
        writer.join();
    }
}