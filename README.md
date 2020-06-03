*本项目仅用于安全研究，禁止使用本项目发起非法攻击，造成的后果使用者负责*

这是一个个人用于复现、公开一些感兴趣、或者影响稍大的漏洞的项目，没有多少技术含量，权当个人技术笔记。

---

### fastjson

该模块主要记录一些fastjson的利用gadget，不过很多gadget并没有记录在案。

##### RCE相关
package：com.threedr3am.bug.fastjson.rce

1. com.threedr3am.bug.fastjson.rce.FastjsonSerialize(TemplatesImpl) 利用条件：fastjson <= 1.2.24 + Feature.SupportNonPublicField
2. com.threedr3am.bug.fastjson.rce.NoNeedAutoTypePoc 利用条件：fastjson < 1.2.48 不需要任何配置，默认配置通杀RCE
3. com.threedr3am.bug.fastjson.rce.HikariConfigPoc(HikariConfig) 利用条件：fastjson <= 1.2.59 RCE，需要开启AutoType
4. com.threedr3am.bug.fastjson.rce.CommonsProxyPoc(SessionBeanProvider) 利用条件：fastjson <= 1.2.61 RCE，需要开启AutoType
5. com.threedr3am.bug.fastjson.rce.JndiConverterPoc(JndiConverter) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
6. com.threedr3am.bug.fastjson.rce.HadoopHikariPoc(HikariConfig) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
7. com.threedr3am.bug.fastjson.rce.IbatisSqlmapPoc(JtaTransactionConfig) 利用条件：fastjson <= 1.2.62 RCE，需要开启AutoType
8. com.threedr3am.bug.fastjson.rce.ShiroPoc(shiro-core) 利用条件：fastjson <= 1.2.66 RCE，需要开启AutoType
...省略若干

##### SSRF相关
package：com.threedr3am.bug.fastjson.ssrf

1. com.threedr3am.bug.fastjson.ssrf.ApacheCxfSSRFPoc(WadlGenerator) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
2. com.threedr3am.bug.fastjson.ssrf.ApacheCxfSSRFPoc2(SchemaHandler) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
3. com.threedr3am.bug.fastjson.ssrf.CommonsJellySSRFPoc(Embedded) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
4. com.threedr3am.bug.fastjson.ssrf.JREJeditorPaneSSRFPoc(JEditorPane) 利用条件：fastjson <= 1.2.66 SSRF，需要开启AutoType
...省略若干

##### DNS域名解析相关
package：com.threedr3am.bug.fastjson.dns

##### Dos拒绝服务相关
package：com.threedr3am.bug.fastjson.dos

##### leak信息泄露相关
package：com.threedr3am.bug.fastjson.leak

---

### jackson
##### RCE相关
package：com.threedr3am.bug.jackson.rce

1. com.threedr3am.bug.jackson.rce.AnterosPoc
2. com.threedr3am.bug.jackson.rce.EhcacheJndi
3. com.threedr3am.bug.jackson.rce.H2Rce
4. com.threedr3am.bug.jackson.rce.HadoopHikariConfigPoc
5. com.threedr3am.bug.jackson.rce.HikariConfigPoc
6. com.threedr3am.bug.jackson.rce.IbatisSqlmapPoc
7. com.threedr3am.bug.jackson.rce.JndiConverterPoc
8. com.threedr3am.bug.jackson.rce.LogbackJndi
...省略若干

##### SSRF
package：com.threedr3am.bug.jackson.ssrf
...省略若干

---

### dubbo

该模块主要记录dubbo相关的漏洞利用、安全加固等

1. com.threedr3am.bug.dubbo.RomePoc 利用条件：存在rome依赖
2. com.threedr3am.bug.dubbo.ResinPoc 利用条件：存在com.caucho:quercus依赖
3. com.threedr3am.bug.dubbo.XBeanPoc 利用条件：存在org.apache.xbean:xbean-naming依赖
4. com.threedr3am.bug.dubbo.SpringAbstractBeanFactoryPointcutAdvisorPoc 利用条件：存在org.springframework:spring-aop依赖

##### dubbo-hessian2-safe-reinforcement
dubbo hessian2安全加固demo，使用黑名单方式禁止部分gadget

---

### padding-oracle-cbc

用Java实现padding-oracle-cbc攻击的一些实验代码记录

1. com.threedr3am.bug.paddingoraclecbc.PaddingOracle ```padding oracle java实现（多组密文实现）```
2. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC ```padding oracle cbc java实现（单组 <= 16bytes 密文实现）```
3. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC2 ```padding oracle cbc java实现（多组密文实现）```
4. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBCForShiro ```shiro padding oracle cbc java实现```

---

### xxe

各种XML解析组件导致XXE的复现，以及其fix代码记录

---

### commons-collections

好几年前学习反序列化的时候瞎写的东西

---

###  security-manager

java security manager的一些绕过实验代码

---

### rmi

rmi相关服务，以及其利用等


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

一些Spring漏洞的复现实验代码记录

1. spring-actuator(jolokia、snake-yaml、h2-hikariCP、eureka)
2. spring-cloud-config-server(CVE-2019-3799)
3. spring-cloud-config-server(CVE-2020-5405)
4. spring-cloud-config-server(CVE-2020-5410)
5. spring-session-data-redis RCE

### apache-poi

apache-poi excel解析漏洞相关记录

### feature

一些攻击的数据特征，本来想法是看看正则等能不能都检测到

### java-compile

java动态编译、操纵字节码的实现代码

### nexus

maven nexus的一些RCE、Auth Bypass漏洞的复现记录

### ShardingSphere-UI

ShardingSphere-UI的一些漏洞记录

1. CVE-2020-1947 (YAML反序列化RCE漏洞)

### shiro

记录了最近shiro被发现的一些认证bypass漏洞

1. bypass shiro <= 1.4.1
2. bypass shiro <= 1.5.2 (CVE-2020-1957)
3. bypass shiro <= 1.5.3 (CVE-2020-11989)