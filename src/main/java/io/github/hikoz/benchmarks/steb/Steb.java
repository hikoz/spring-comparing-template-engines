package io.github.hikoz.benchmarks.steb;

import java.io.PrintWriter;
import java.util.Map;

public interface Steb {
  StebTemplate template(String viewName) throws Exception;

  Map<String, ?> attributes();

  interface StebTemplate {
    void writeTo(PrintWriter writer, Map<String, Object> model);
  }
}
