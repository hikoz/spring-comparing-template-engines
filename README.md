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
* [Scalate](http://scalate.fusesource.org)  - 1.7.0

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
* stop verification in each iteration
* stop using MockMvc. directly call ViewResolver
* sort and prettify results
* apply steb to all template engine

* [Twirl](https://github.com/spray/twirl)  - 1.0.2
* groovy-template
* clojure-str
* [hiccup "1.0.5"]
* [stencil "0.3.4"]
* [selmer "0.6.9"]
* [tinsel "0.4.0"]
* [me.shenfeng/mustache "1.1"]])


## Benchmarking
Rendered with 10 concurrent requests.
Pebble is the fastest.

we must use Pebble.
```
Benchmark                                       (engine)   Mode  Samples    Score  Score error   Units
i.g.h.b.s.TemplateBenchmarks.templateBench        string  thrpt        7  123.501        9.292  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench    handlebars  thrpt        7   19.121        0.387  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         rythm  thrpt        7    6.073        0.180  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench     thymeleaf  thrpt        7    6.604        0.229  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench      mustache  thrpt        7   44.024        2.634  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench     jmustache  thrpt        7   30.653        0.654  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench       scalate  thrpt        7   11.206        1.008  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          httl  thrpt        7   14.915        0.116  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench      velocity  thrpt        7   38.733        2.752  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          jade  thrpt        7   10.950        0.078  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench     jade-steb  thrpt        7   12.510        0.222  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         jtwig  thrpt        7   12.602        0.332  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench        pebble  thrpt        7   49.266        2.965  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench    freemarker  thrpt        7   38.462        0.682  ops/m
```
