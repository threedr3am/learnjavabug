*本项目仅用于安全研究，禁止使用本项目发起非法攻击，造成的后果使用者负责*

---

### fastjson
##### RCE
package：com.threedr3am.bug.fastjson.rce

1. com.threedr3am.bug.fastjson.rce.FastjsonSerialize(TemplatesImpl) 利用条件：fastjson <= 1.2.24 + Feature.SupportNonPublicField
2. com.threedr3am.bug.fastjson.rce.NoNeedAutoTypePoc 利用条件：fastjson < 1.2.48 不需要任何配置，默认配置通杀RCE
3. com.threedr3am.bug.fastjson.rce.HikariConfigPoc(HikariConfig) 利用条件：fastjson <= 1.2.59 RCE，需要开启AutoType
4. com.threedr3am.bug.fastjson.rce.CommonsProxyPoc(SessionBeanProvider) 利用条件：fastjson <= 1.2.61 RCE，需要开启AutoType
5. com.threedr3am.bug.fastjson.rce.JndiConverterPoc(JndiConverter) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
6. com.threedr3am.bug.fastjson.rce.HadoopHikariPoc(HikariConfig) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
7. com.threedr3am.bug.fastjson.rce.IbatisSqlmapPoc(JtaTransactionConfig) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
8. com.threedr3am.bug.fastjson.rce.ShiroPoc(shiro-core) 利用条件：fastjson <= 1.2.66 RCE，需要开启AutoType

##### SSRF
package：com.threedr3am.bug.fastjson.ssrf

1. com.threedr3am.bug.fastjson.ssrf.ApacheCxfSSRFPoc(WadlGenerator) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
2. com.threedr3am.bug.fastjson.ssrf.ApacheCxfSSRFPoc2(SchemaHandler) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
3. com.threedr3am.bug.fastjson.ssrf.CommonsJellySSRFPoc(Embedded) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
4. com.threedr3am.bug.fastjson.ssrf.JREJeditorPaneSSRFPoc(JEditorPane) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType

---

### jackson
##### RCE
package：com.threedr3am.bug.jackson.rce

1. com.threedr3am.bug.jackson.rce.AnterosPoc
2. com.threedr3am.bug.jackson.rce.EhcacheJndi
3. com.threedr3am.bug.jackson.rce.H2Rce
4. com.threedr3am.bug.jackson.rce.HadoopHikariConfigPoc
5. com.threedr3am.bug.jackson.rce.HikariConfigPoc
6. com.threedr3am.bug.jackson.rce.IbatisSqlmapPoc
7. com.threedr3am.bug.jackson.rce.JndiConverterPoc
8. com.threedr3am.bug.jackson.rce.LogbackJndi

##### SSRF
package：com.threedr3am.bug.jackson.ssrf

---

### dubbo
1. com.threedr3am.bug.dubbo.RomePoc 利用条件：存在rome依赖
2. com.threedr3am.bug.dubbo.ResinPoc 利用条件：存在com.caucho:quercus依赖
3. com.threedr3am.bug.dubbo.XBeanPoc 利用条件：存在org.apache.xbean:xbean-naming依赖
4. com.threedr3am.bug.dubbo.SpringAbstractBeanFactoryPointcutAdvisorPoc 利用条件：存在org.springframework:spring-aop依赖

#### dubbo/dubbo-hessian2-safe-reinforcement
dubbo hessian2安全加固demo，使用黑名单方式禁止部分gadget

---

### padding-oracle-cbc
1. com.threedr3am.bug.paddingoraclecbc.PaddingOracle ```padding oracle java实现（多组密文实现）```
2. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC ```padding oracle cbc java实现（单组 <= 16bytes 密文实现）```
3. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC2 ```padding oracle cbc java实现（多组密文实现）```
4. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBCForShiro ```shiro padding oracle cbc java实现```

---

### xxe
paclage：com.threedr3am.bug.xxe

---

### commons-collections
package：com.threedr3am.bug.collections

---

###  security-anager
package：com.threedr3am.bug.security.manager

---

### rmi
package：com.threedr3am.bug.rmi

---

### tomcat
tomcat相关漏洞

#### ajp-bug
tomcat ajp协议相关漏洞 
1. com.threedr3am.bug.tomcat.ajp 任意文件读取和jsp渲染RCE CVE-2020-1938

---

### cas

cas相关漏洞 

1. cas-4.1.x~4.1.6 反序列化漏洞（利用默认密钥）
2. cas-4.1.7~4.2.x 反序列化漏洞（需要知道加密key和签名key）

---

### spring
1. spring-actuator(jolokia、snake-yaml、h2-hikariCP、eureka)
2. spring-cloud-config-server(CVE-2019-3799)
3. spring-cloud-config-server(CVE-2020-5405)