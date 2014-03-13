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
You need Java 6 and Maven 3 to build and run this project.

### TODO

+ mvn package to create executable jar.
+ use custom base name of messagesource in HTTL
+ i18n for jade
+ fix NPE in Scalate
+ how to compile jasper in Java Application
+ measure rendering time.
+ pretty print
+ use good benchmark library if exists

## Benchmarking

Rendered in 5 seconds with 10 concurrent requests
```
{handlebars=31385, rythm=19732, thymeleaf=9038, mustache=79612, httl=0, velocity=108777, freemarker=92269, jade=0}
```
