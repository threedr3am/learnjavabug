package com.threedr3am.bug.xxe;

/**
 * 各种feature
 *
 * @author xuanyh
 */
public interface Features {

  String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

  /**
   * 是否允许使用通用实体
   */
  String FEATURE2 = "http://xml.org/sax/features/external-general-entities";

  /**
   * 是否允许使用参数实体
   */
  String FEATURE3 = "http://xml.org/sax/features/external-parameter-entities";

  /**
   * 是否允许加载外部DTD实体
   */
  String FEATURE4 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

  /**
   * 是否启用安全性处理
   */
  String FEATURE_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

  /**
   * 是否允许使用外部DTD实体
   */
  String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";

  String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";

  String ACCESS_EXTERNAL_STYLESHEET = "http://javax.xml.XMLConstants/property/accessExternalStylesheet";
}
