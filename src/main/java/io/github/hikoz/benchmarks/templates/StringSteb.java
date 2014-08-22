package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.util.Map;

import com.google.common.base.Function;
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
      StringBuilder b = new StringBuilder(10000);
      b.append("<title>").append(loader.message("example.title")).append("</title>");
      b.append("<h1>").append(loader.message("example.title")).append("</h1>");
      for (Presentation p : (Iterable<Presentation>) model.get("presentations")) {
        b.append("<h3 class=\"panel-title\">")
            .append(p.getTitle())
            .append(" - ")
            .append(p.getSpeakerName())
            .append("</h3><div class=\"panel-body\">")
            .append(p.getSummary())
            .append("</div");
      }
      w.write(b.toString());
    };
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of("i18n",
        (Function<String, String>) (key) -> loader.message(key));
  }
}
