/**
 * overlays：想要自定义啥东西，从target/cas自己捞，classes里面的文件，如果是类， 则按照包名和类名在src/main/java放，若是资源文件，则放置到resources
 *
 * 启动的话，需要在project structure，即项目结构设置中Facets的web添加当前项目的资源目录(webapp)进去，然后配置artifacts，添加web application
 * archive和web application exploded，前者对应war包，后者对应war解压目录，tomcat部署需要
 *
 * @author threedr3am
 */
package com.threedr3am.bug.cas;