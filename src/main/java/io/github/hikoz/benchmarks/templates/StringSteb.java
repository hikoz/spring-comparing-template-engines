package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.jeroenreijn.examples.model.Presentation;

public class StringSteb implements Steb {
  private StebLoader loader;

  public StringSteb(StebLoader loader) {
    this.loader = loader;
  }

  @SuppressWarnings("unchecked")
  public StebTemplate template(String viewName) throws Exception {
    return (w, model) -> {
      w.write("<title>");
      w.write(loader.message("example.title"));
      w.write("</title>");
      w.write("<h1>");
      w.write(loader.message("example.title"));
      w.write("</h1>");
      for (Presentation p : (Iterable<Presentation>) model.get("presentations")) {
        w.write("<h3 class=\"panel-title\">");
        w.write(p.getTitle());
        w.write(" - ");
        w.write(p.getSpeakerName());
        w.write("</h3><div class=\"panel-body\">");
        w.write(p.getSummary());
        w.write("</div");
      }
    };
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of();
  }
}
