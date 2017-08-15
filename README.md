# Apache-Ignite-Cache-Session-Distribution-Spring-security-Samples
Java based samples of various usages of Apache Ignite in-memory distributed data fabric

## What is Apache Ignite?
Ignite is a memory-centric data platform

    that is strongly consistent
    and highly available
    with powerful SQL,
    key-value and processing APIs
for more info follow Apache Ignite official web site https://ignite.apache.org/

## Modules
### Command Line Samples
1. Ignite distributed cache
2. Ignite compute
3. Ignite data structures
4. Ignite in-memory file system
5. Ignite messaging
6. Ignite distributed services

Can be executed through command line

### Web based session distribution
This demonstrates http servlet session distribution through multiple server instances. 
Session values can be accessed from any sever. In this example session is partitioned through out nodes.
Ignite allows to session replication also. Please check ignite documentation for more details.

### Spring MVC + Spring security session distribution
This demonstrates login session distribution through multiple server instances in Spring MVC + Spring security application.
Login session can be accessed from any server node.

Note: To test distributed deployments please deploy servers in separate domains. Otherwise it will raise 'JSEESSIONID' cookie issues.
Please look into following example.

eg.
Not to deploy:
    first server: localhost:8080/ignite-spring/
    second server: localhost:8180/ignite-spring/
    
How to deploy:
    first server: one.localhost:8080/ignite-spring/
    second server: two.localhost:8180/ignite-spring/
    
(You can create virtual hosts/virtual domains in window environment by editing C:\Windows\System32\drivers\etc\hosts file.)
 
## Environment
    > Java version: JDK 8
    > Build tool: maven 3+
    > Appication/servlet container: Apache Tomcat (for Web based session distribution & Spring MVC + Spring security session distribution)
 
 