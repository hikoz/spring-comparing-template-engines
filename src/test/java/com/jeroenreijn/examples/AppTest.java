package com.jeroenreijn.examples;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.ArrayList;
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
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext wac;

  private LinkedHashMap<String, Long> counters;

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }

  @Test
  public void benchmark() throws Exception {
    counters = new LinkedHashMap<String, Long>();
    run("handlebars");
    run("rythm");
    run("thymeleaf");
    run("mustache");
    run("jmustache");
    // run("httl");
    run("velocity");
    run("freemarker");
    run("jade");
    System.out.println(counters);
  }

  int threads = 10;

  private void run(final String name) throws Exception {
    final AtomicBoolean running = new AtomicBoolean(true);
    Callable<Long> c = new Callable<Long>() {
      public Long call() throws Exception {
        long count = 0L;
        while (running.get()) {
          String expected = "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
          mockMvc
              .perform(get("/" + name))
              .andExpect(
                  header().string("Content-Type", "text/html;charset=UTF-8"))
              .andExpect(status().isOk())
              .andExpect(content().string(containsString("<h1>こんにちは")))
              .andExpect(content().string(containsString(expected)));
          count++;
        }
        return count;
      }
    };
    System.out.println("warmup:" + name);
    counter(running, c, 2);
    System.out.println("start:" + name);
    long sum = counter(running, c, 5);
    System.out.println("stop :" + name);
    counters.put(name, sum);
  }

  private long counter(final AtomicBoolean running,
      Callable<Long> c, int sec) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    running.set(true);
    List<Future<Long>> l = new ArrayList<Future<Long>>();
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
    System.gc();
    return sum;
  }

}
