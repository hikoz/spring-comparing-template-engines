package io.github.hikoz.benchmarks.spring;

import org.junit.Test;

public class TemplateBenchmarksTest {

  @Test
  public void test() throws Exception {
    TemplateBenchmarks target = new TemplateBenchmarks();
    target.engine = "httl";
    target.setup();
    target.templateBench();
  }

}
