package com.jeroenreijn.examples;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
  @Autowired
  MessageSource message;

  private int count = 1000;

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(wac).build();
  }
  @After
  public void after() {
    System.gc();
  }

  @Test
  public void handlebars() throws Exception {
    run("handlebars");
  }

  @Test
  public void rythm() throws Exception {
    run("rythm");
  }

  @Test
  public void thymeleaf() throws Exception {
    run("thymeleaf");
  }

  @Test
  public void mustache() throws Exception {
    run("mustache");
  }

  @Test
  public void httl() throws Exception {
    run("httl");
  }

  @Test
  public void velocity() throws Exception {
    run("velocity");
  }

  @Test
  public void freemarker() throws Exception {
    run("freemarker");
  }

  @Test
  public void jade() throws Exception {
    run("jade");
  }

  private void run(final String name) throws Exception {
    int threads = 10;
    ExecutorService pool = Executors.newFixedThreadPool(threads);

    List<Future<Void>> l = new ArrayList<Future<Void>>();
    for (int i = 0; i < count; ++i) {
      l.add(pool.submit(new Callable<Void>() {
        public Void call() throws Exception {
          String expected = "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
          mockMvc.perform(get("/" + name))
          .andExpect(header().string("Content-Type", "text/html;charset=UTF-8"))
              .andExpect(status().isOk())
              .andExpect(content().string(containsString("<h1>こんにちは")))
              .andExpect(content().string(containsString(expected)));
          return null;
        }
      }));
    }
    pool.shutdown();
    pool.awaitTermination(10, TimeUnit.SECONDS);

    for (Future<Void> f : l) {
      f.get();
    }
  }

}
