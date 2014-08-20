#Comparing Template engines for Spring MVC

This is a demo project to show the differences among several Java template engines in combination with Spring MVC. Template engines used in this project are:

* [Freemarker](http://www.freemarker.org) - 2.3.20
* [Velocity](http://velocity.apache.org) - 1.7
* [Thymeleaf](http://www.thymeleaf.org/) - 2.1.3.RELEASE
* [JMustache](https://github.com/samskivert/jmustache) - 1.9
* [Mustache.java](https://github.com/spullara/mustache.java) - 0.8.16
* [Jade](https://github.com/neuland/jade4j) - 0.4.0
* [Handlebars](https://github.com/jknack/handlebars.java) - 1.3.1
* [JTwig](https://github.com/lyncode/jtwig) - 2.1.7
* [Rythm](http://rythmengine.org/) - 1.0
* [HTTL](http://httl.github.io/en/) - 1.0.11
* -- JSP + JSTL - v1.2 --
* -- [Scalate](http://scalate.fusesource.org)  - 1.6.1 --


## Build and run
You need Java 8 and Maven 3 to build and run this project.

### TODO

+ create package as executable jar.
+ pretty print. per millisecond
+ use good benchmark library if exists

+ Scalate: fix NPE
+ JSP: how to compile by jasper in Java Application

## Benchmarking

Rendered in 10 seconds with 10 concurrent requests.
mustache.java is the fastest.
```
      string: 1165496
    mustache:  411902
    velocity:  371813
  freemarker:  359217
   jmustache:  258177
  handlebars:  191117
        httl:  136301
       jtwig:  121106
        jade:  108183
       rythm:   60847
   thymeleaf:   60683
```
