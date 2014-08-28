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
* sort and prettify results
* apply steb to all template engine
* performance tuning for Steb

* [Twirl](https://github.com/spray/twirl)  - 1.0.2
* groovy-template
* clojure-str
* [hiccup "1.0.5"]
* [stencil "0.3.4"]
* [selmer "0.6.9"]
* [tinsel "0.4.0"]
* [me.shenfeng/mustache "1.1"]])


## Benchmarking
Rendered with 30 concurrent requests.

Mustache.java is the fastest.
string version simplely write to PrintWriter.
I love Pebble because it has macro.
pebble-spring3 has also good implementation of ViewResolver.
```
Benchmark                                            (engine)   Mode  Samples   Score  Score error   Units
i.g.h.b.s.TemplateBenchmarks.templateBench             string  thrpt        5  105.649        5.274  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         handlebars  thrpt        5   23.956        0.975  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench    handlebars-steb  thrpt        5   26.025        1.633  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench              rythm  thrpt        5    6.180        0.535  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          thymeleaf  thrpt        5    6.919        0.304  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench           mustache  thrpt        5   83.881       21.576  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          jmustache  thrpt        5   42.358        2.343  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench            scalate  thrpt        5   12.682        0.304  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench               httl  thrpt        5   16.356        4.518  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench           velocity  thrpt        5   69.507        4.096  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench               jade  thrpt        5   11.495        1.023  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          jade-steb  thrpt        5   12.737        4.439  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench              jtwig  thrpt        5   10.562        2.263  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench             pebble  thrpt        5   79.252        6.240  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench        pebble-steb  thrpt        5   67.333        5.565  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         freemarker  thrpt        5   52.210        2.969  ops/ms
```
