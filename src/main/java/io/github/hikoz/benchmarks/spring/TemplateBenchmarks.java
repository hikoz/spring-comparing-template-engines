package io.github.hikoz.benchmarks.spring;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgsAppend = { "-Xmx2048m", "-server",
    "-XX:+AggressiveOpts" })
@Warmup(iterations = 8, time = 8, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(200)
public class TemplateBenchmarks {
  @Param({
      "string",
      "handlebars",
      "handlebars-steb",
      "rythm",
      "rythm-steb",
      "thymeleaf",
      "mustache",
      "jmustache",
      "scalate",
      "httl",
      "velocity",
      "jade",
      "jade-steb",
      "jtwig",
      "pebble",
      "pebble-steb",
      "freemarker",
  })
  String engine;
  private AnnotationConfigWebApplicationContext context;

  private DispatcherServlet servlet;

  private MockServletContext servletContext;

  @Setup
  public void setup() throws ServletException {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");

    context = new AnnotationConfigWebApplicationContext();
    context.register(App.class);
    servletContext = new MockServletContext("src/main/webapp",
        new FileSystemResourceLoader());
    context.setServletContext(servletContext);
    context.refresh();
    servlet = new DispatcherServlet(context);
    servlet.init(new MockServletConfig(servletContext));
  }

  @TearDown
  public void teardown() {
    context.close();
  }

  @Benchmark
  public MockHttpServletResponse templateBench() throws Exception {
    MockHttpServletResponse res = new MockHttpServletResponse();
    servlet.service(new MockHttpServletRequest(servletContext, "GET", "/" + engine), res);
    return res;
  }

}
