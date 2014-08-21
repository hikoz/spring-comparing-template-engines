package com.jeroenreijn.examples;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = App.class)
public class AppTest {
  MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;
  private LinkedHashMap<String, Long> counters;
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY,
        "TRACE");
  }

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  List<String> templates = Arrays.asList(
      "string",
      "handlebars",
      "rythm",
      "thymeleaf",
      "mustache",
      "jmustache",
      // "scalate",
      "httl",
      "velocity",
      "freemarker",
      "jade",
      "jtwig",
      "pebble",
      "freemarker");

  @Test
  public void simple() throws Exception {
    for (String t : templates) {
      System.out.println(t);
      render(t);
    }
  }

  @Test
  public void benchmark() throws Exception {
    counters = new LinkedHashMap<>();
    for (String t : templates) {
      run(t);
    }
    counters
        .entrySet()
        .stream()
        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
        .forEach(e -> System.out.printf("%12s:%8d\n", e.getKey(), e.getValue()));
  }

  int threads = 10;
  int SEC = 3;

  private void run(final String name) throws Exception {
    final AtomicBoolean running = new AtomicBoolean(true);
    Callable<Long> c = new Callable<Long>() {
      public Long call() throws Exception {
        long count = 0L;
        while (running.get()) {
          render(name);
          count++;
        }
        return count;
      }
    };
    System.out.println("warmup:" + name);
    counter(running, c, SEC);
    System.out.println("start:" + name);
    long sum = counter(running, c, SEC);
    System.out.println("stop :" + name);
    counters.put(name, sum);
    System.gc();
  }

  void render(final String name) throws Exception {
    String expected = "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
    mockMvc
        .perform(get("/" + name))
        .andExpect(
            header().string("Content-Type", "text/html;charset=UTF-8"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("<h1>こんにちは")))
        .andExpect(content().string(containsString(expected)));
  }

  private long counter(final AtomicBoolean running,
      Callable<Long> c, int sec) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    running.set(true);
    List<Future<Long>> l = new ArrayList<>();
    for (int i = 0; i < threads; ++i) {
      l.add(pool.submit(c));
    }
    pool.shutdown();
    Thread.sleep(sec * 1000L);
    running.set(false);
    pool.awaitTermination(1, TimeUnit.SECONDS);
    long sum = 0L;
    try {
      for (Future<Long> f : l) {
        sum += f.get();
      }
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return sum;
  }

}
