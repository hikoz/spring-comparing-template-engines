#Comparing Template engines for Spring MVC

This is a demo project to show the differences among several Java template engines in combination with Spring MVC. Template engines used in this project are:

* [Freemarker](http://www.freemarker.org) - 2.3.20
* [Velocity](http://velocity.apache.org) - 1.7
* [Thymeleaf](http://www.thymeleaf.org/) - 2.1.2.RELEASE
* Mustache - Based on [JMustache](https://github.com/samskivert/jmustache) - 1.8
* [Mustache.java](https://github.com/spullara/mustache.java) - 0.8.14
* [Jade](https://github.com/neuland/jade4j) - 0.4.0
* [Rythm](http://rythmengine.org/) - 1.0
* [Handlebars](https://github.com/jknack/handlebars.java) - 1.3.0
* -- [HTTL](http://httl.github.io/en/) - 1.0.11 --
* -- JSP + JSTL - v1.2 --
* -- [Scalate](http://scalate.fusesource.org)  - 1.6.1 --


## Build and run
You need Java 6 and Maven 3 to build and run this project.

### TODO

+ mvn package to create executable jar.
+ measure rendering time.
+ pretty print
+ use good benchmark library if exists

+ HTTL: use custom base name of messagesource
+ Scalate: fix NPE
+ JSP: how to compile by jasper in Java Application

## Benchmarking

Rendered in 10 seconds with 10 concurrent requests
mustache.java is the fastest.
```
{str=543348, handlebars=92078, rythm=40020, thymeleaf=33270, mustache=196417,
 jmustache=129424, velocity=169658, freemarker=168892, jade=67086}
```
