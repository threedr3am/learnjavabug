### Spring Boot和Spring Cloud版本匹配参考

Spring Boot : Spring Cloud
- 1.2.x : Angel版本 (snake-yaml、jolokia pass)
- 1.3.x : Brixton版本 (jolokia pass)
- 1.4.x : Camden版本 (snake-yaml、jolokia pass)
- 1.5.x : Dalston版本、Edgware版本 (snake-yaml、jolokia pass) (need to set management:security:enabled: false)
- 2.0.x : Finchley版本 (hikariCP+h2 pass) (need to set management:security:enabled: false, management:endpoint:restart:enabled: true, management:endpoints:web:exposure:include: env,restart)
- 2.1.x : Greenwich.SR2

https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies