package com.threedr3am.bug.tomcat.sync.session.payload;

/**
 * @author threedr3am
 */
public interface Payload {
    Object getObject(String... command) throws Exception;
}
