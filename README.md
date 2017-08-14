# Apache-Ignite-Cache-Session-Distribution-Spring-security-Samples
Java based samples of various usages of Apache Ignite in-memory distributed data fabric

## What is Apache Ignite?
Ignite is a memory-centric data platform

    that is strongly consistent
    and highly available
    with powerful SQL,
    key-value and processing APIs
Find more info on official web site: https://ignite.apache.org/

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

 Known issues:
 Integration issue can be found on this link - https://stackoverflow.com/questions/45648884/apache-ignite-spring-secutiry-error
 
## Environment
    > Java version: JDK 8
    > Build tool: maven 3+
    > Appication/servlet container: Apache Tomcat (for Web based session distribution & Spring MVC + Spring security session distribution)
 
 
