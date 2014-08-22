package io.github.hikoz.benchmarks.spring;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.concurrent.TimeUnit;

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
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.jeroenreijn.examples.App;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgsAppend = { "-Xmx2048m", "-server",
    "-XX:+AggressiveOpts" })
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(30)
public class TemplateBenchmarks {
  MockMvc mockMvc;

  @Param({
      "string",
      "handlebars",
      "handlebars-steb",
      "rythm",
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

  boolean render(final String name) throws Exception {
    String expected = "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
    mockMvc
        .perform(get("/" + name))
        .andExpect(
            header().string("Content-Type", "text/html;charset=UTF-8"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("<h1>こんにちは")))
        .andExpect(content().string(containsString(expected)));
    return true;
  }

  @Setup
  public void setup() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");

    context = new AnnotationConfigWebApplicationContext();
    context.register(App.class);
    MockServletContext sc = new MockServletContext("src/main/webapp",
        new FileSystemResourceLoader());
    context.setServletContext(sc);
    context.refresh();
    mockMvc = webAppContextSetup(context).build();
  }

  @TearDown
  public void teardown() {
    context.close();
  }

  @Benchmark
  public boolean templateBench() throws Exception {
    return render(engine);
  }

}
