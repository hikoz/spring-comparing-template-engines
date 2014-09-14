package io.github.hikoz.benchmarks.spring;

import static org.assertj.core.api.Assertions.*;

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
      assertThat(res.getStatus()).isEqualTo(200);
      assertThat(res.getHeader("Content-Type")).isEqualTo("text/html;charset=UTF-8");
      String c = res.getContentAsString();
      assertThat(c).contains("<h1>こんにちは");
      assertThat(c).contains("<h3 class=\"panel-title\">"
        + "Shootout! Template engines on the JVM - Jeroen Reijn</h3>");
    }
    target.teardown();
  }
}
