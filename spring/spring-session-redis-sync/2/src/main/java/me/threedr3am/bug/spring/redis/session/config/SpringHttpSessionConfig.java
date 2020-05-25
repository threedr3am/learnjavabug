package me.threedr3am.bug.spring.redis.session.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author threedr3am
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "threedr3am-session", maxInactiveIntervalInSeconds = 2 * 60 * 60)
public class SpringHttpSessionConfig {

}
