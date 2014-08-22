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
Rendered with 30 concurrent requests.

Pebble is awesome.
string version is simple StringBuilder version.
Pebble has almost same speed as raw StringBuilder.
Use Pebble or stop using Spring.
pebble-spring3 is also good implementation of ViewResolver.
```
Benchmark                                            (engine)   Mode  Samples   Score  Score error   Units
i.g.h.b.s.TemplateBenchmarks.templateBench             string  thrpt        5  51.000        5.970  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         handlebars  thrpt        5  18.278        4.944  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench    handlebars-steb  thrpt        5  18.045       10.837  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench              rythm  thrpt        5   5.017        1.158  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          thymeleaf  thrpt        5   5.679        0.475  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench           mustache  thrpt        5  41.663        5.805  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          jmustache  thrpt        5  27.676        4.954  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench            scalate  thrpt        5  11.142        1.912  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench               httl  thrpt        5  14.953        2.801  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench           velocity  thrpt        5  37.464        7.600  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench               jade  thrpt        5   9.753        1.712  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench          jade-steb  thrpt        5  11.694        0.649  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench              jtwig  thrpt        5  11.923        2.583  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench             pebble  thrpt        5  49.517        2.838  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench        pebble-steb  thrpt        5  43.667        2.471  ops/ms
i.g.h.b.s.TemplateBenchmarks.templateBench         freemarker  thrpt        5  34.353        3.960  ops/ms
```
