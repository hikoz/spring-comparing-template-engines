#Comparing Template engines for Spring MVC

This is a demo project to show the differences among several Java template engines in combination with Spring MVC.
Template engines used in this project are:

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
* [Pebble](http://www.mitchellbosecke.com/pebble/) - 1.0.0

## Build and run
You need Java 8 to build and run this project.
```
gradle run
```
or
```
./gradlew run
```
AppTest is useful for checking implementation.

### TODO
* stop verify each iteration
* stop using MockMvc. directly call ViewResolver
* create AbstractTemplateEngine and GenericViewResolver
* compare performance of each ViewResolver itself

* [Scalate](http://scalate.fusesource.org)  - 1.6.1
* [Twirl](https://github.com/spray/twirl)  - 1.0.2
* groovy-template
* clojure-str
* [hiccup "1.0.5"]
* [stencil "0.3.4"]
* [selmer "0.6.9"]
* [tinsel "0.4.0"]
* [me.shenfeng/mustache "1.1"]])


## Benchmarking

Rendered in 10 seconds with 10 concurrent requests.
Pebble is the fastest.

we must use Pebble.
```
      string: 1055217
      pebble:  536698
    mustache:  432172
    velocity:  390009
  freemarker:  360053
   jmustache:  273438
  handlebars:  190972
        httl:  149052
       jtwig:  130350
        jade:  102903
       rythm:   57297
   thymeleaf:   56270
```
