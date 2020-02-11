### fastjson poc
1. com.threedr3am.bug.fastjson.FastjsonSerialize(TemplatesImpl) 利用条件：fastjson <= 1.2.24 + Feature.SupportNonPublicField
2. com.threedr3am.bug.fastjson.NoNeedAutoTypePoc 利用条件：fastjson < 1.2.48 不需要任何配置，默认配置通杀RCE
3. com.threedr3am.bug.fastjson.HikariConfigPoc(HikariConfig) 利用条件：fastjson <= 1.2.59 RCE，需要开启AutoType
4. com.threedr3am.bug.fastjson.CommonsProxyPoc(SessionBeanProvider) 利用条件：fastjson <= 1.2.61 RCE，需要开启AutoType

### jackson poc
package：com.threedr3am.bug.jackson

### dubbo
1. com.threedr3am.bug.dubbo.RomePoc 利用条件：存在rome依赖
2. com.threedr3am.bug.dubbo.ResinPoc 利用条件：存在com.caucho:quercus依赖
3. com.threedr3am.bug.dubbo.XBeanPoc 利用条件：存在org.apache.xbean:xbean-naming依赖
4. com.threedr3am.bug.dubbo.SpringAbstractBeanFactoryPointcutAdvisorPoc 利用条件：存在org.springframework:spring-aop依赖

### Padding Oracle CBC
1. com.threedr3am.bug.paddingoraclecbc.PaddingOracle ```padding oracle java实现（多组密文实现）```
2. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC ```padding oracle cbc java实现（单组 <= 16bytes 密文实现）```
3. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBC2 ```padding oracle cbc java实现（多组密文实现）```
4. com.threedr3am.bug.paddingoraclecbc.PaddingOracleCBCForShiro ```shiro padding oracle cbc java实现```

### XXE
paclage：com.threedr3am.bug.xxe

### Commons-Collections
package：com.threedr3am.bug.collections3

### Java Security Manager
package：com.threedr3am.bug.security.manager