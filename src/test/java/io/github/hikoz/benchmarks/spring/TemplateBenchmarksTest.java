package io.github.hikoz.benchmarks.spring;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;
import org.openjdk.jmh.annotations.Param;
import org.springframework.mock.web.MockHttpServletResponse;

public class TemplateBenchmarksTest {
  @Test
  public void allTemplates() throws Exception {
    Field field = TemplateBenchmarks.class.getDeclaredField("engine");
    TemplateBenchmarks target = new TemplateBenchmarks();
    target.setup();
    for (String engine : field.getAnnotation(Param.class).value()) {
      target.engine = engine;
      MockHttpServletResponse res = target.templateBench();
      assertThat(res.getStatus(), is(200));
      assertThat(res.getHeader("Content-Type"), is("text/html;charset=UTF-8"));
      String c = res.getContentAsString();
      assertThat(engine, c, containsString("<h1>こんにちは"));
      assertThat(engine, c, containsString("<h3 class=\"panel-title\">"
          + "Shootout! Template engines on the JVM - Jeroen Reijn</h3>"));
    }
    target.teardown();
  }
}
