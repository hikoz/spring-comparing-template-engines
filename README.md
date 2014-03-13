#Comparing Template engines for Spring MVC

This is a demo project to show the differences among several Java template engines in combination with Spring MVC. Template engines used in this project are:

* [Freemarker](http://www.freemarker.org) - 2.3.20
* [Velocity](http://velocity.apache.org) - 1.7
* [Thymeleaf](http://www.thymeleaf.org/) - 2.1.2.RELEASE
* Mustache - Based on [JMustache](https://github.com/samskivert/jmustache) - 1.8
* [Jade](https://github.com/neuland/jade4j) - 0.4.0
* [Rythm](http://rythmengine.org/) - 1.0
* [HTTL](http://httl.github.io/en/) - 1.0.11
* [Handlebars](https://github.com/jknack/handlebars.java) - 1.3.0
* -- JSP + JSTL - v1.2 --
* -- [Scalate](http://scalate.fusesource.org)  - 1.6.1 --


## Build and run
You need Java 7 and Maven 3 to build and run this project.
TODO mvn package to create executable jar.
TODO use custom base name of messagesource
TODO i18n for jade
TODO fix NPE in Scalate
TODO how to compile jasper in Java Application

## Benchmarking

TODO measure rendering time.
TODO warmup
TODO pretty print
TODO use good benchmark library if exists

On my local machine with the following specs I did some benchmarks.

```
Mac OS X Version 10.8.5
2.53 GHz Intel Core 2 Duo
Java(TM) SE Runtime Environment (build 1.7.0_21-b12)
Tomcat 7.0.41 with 512M RAM
```

For creating my benchmarks I used ApacheBench, Version 2.3 <$Revision: 1430300 $> with the following settings:

```
ab -n 25000 -c 25 http://localhost:8080/jsp
```
With 25 concurrent requests and 25.000 requests in total this resulted in the following numbers:

```
Thymeleaf				21.8436 seconds
Jade4j					13.7044 seconds
Scalate - Scaml			12.1704 seconds
Mustache (JMustache)	8.8148 seconds
Freemarker				8.5574 seconds
Velocity				8.5052 seconds
JSP						8.8278 seconds

```
