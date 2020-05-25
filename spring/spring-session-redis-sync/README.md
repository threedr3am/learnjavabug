### spring-boot session同步最佳实践脚手架spring-session-data-redis的安全隐患问题

*一年前半前在某互联网医疗企业写过一个项目，当时项目中对接了微信的OAuth，为了性能考虑，不惜破坏了OAuth的规范，对其使用了session，
但因为系统部署是集群模式的，在A机器上做好OAuth认证初始化的session，在B机器上是不存在的，导致在做了负载均衡访问的集群环境中，
需要做多次重复冗余的OAuth认证，最终为了性能考虑，做了一番调研，选择了开源社区中比较成熟，受众比较多的spring-session-data-redis.*

*最近看了下spring-session-data-redis的源码发现，其默认使用了JDK的原生序列化方式对session值进行了序列化，然后缓存到redis中，
集群中的每一台机器，在用户访问后，将会从redis中取出缓存的数据，并反序列化回session，这个过程存在很大的安全隐患，恶意用户利用
redis未授权访问或弱口令等缺陷，取得对其读写的权力，然后对其写入恶意序列化数据，在集群机器对其数据进行反序列化时，将会执行恶意代码。*

*很明显，spring-session-data-redis不在意其反序列化安全问题，认为其安全问题都是由于redis的未授权访问或弱口令等缺陷导致的。*

1. run server-1 & server-2
2. GET http://127.0.0.1:30001/cache?data=threedr3am
3. GET http://127.0.0.1:30002/cache

上述步骤后可以看到session数据已经同步成功

4. run me.threedr3am.bug.spring.redis.session.Main
5. 把得到的十六进制序列化数据通过redis客户端，设置到redis存储的session value
6. GET http://127.0.0.1:30002/cache

这个时候就触发了反序列化，计算器弹出来了！