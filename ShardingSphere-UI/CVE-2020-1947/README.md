### *ShardingSphere-UI YAML反序列化*

CVE-2020-1947

#### 二进制运行
- git clone https://github.com/apache/incubator-shardingsphere.git；
- 运行 mvn clean install -Prelease；
- 获取安装包 /sharding-distribution/shardingsphere-ui-distribution/target/apache-shardingsphere-incubating-${latest.release.version}-sharding-ui-bin.tar.gz；
- 解压缩后运行bin/start.sh；
- 访问http://localhost:8088/。

```
server.port=8088

user.admin.username=admin
user.admin.password=admin
```