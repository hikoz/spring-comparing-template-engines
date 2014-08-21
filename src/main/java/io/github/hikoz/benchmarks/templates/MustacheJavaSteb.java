package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.Reader;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

public class MustacheJavaSteb implements Steb {
  private DefaultMustacheFactory compiler;
  private StebLoader loader;

  public MustacheJavaSteb(StebLoader loader) {
    this.loader = loader;
    compiler = new DefaultMustacheFactory() {
      @Override
      public Reader getReader(String resourceName) {
        return loader.getReader(resourceName);
      }
    };
  }

  public StebTemplate template(String viewName) throws Exception {
    Mustache template = compiler.compile(viewName);
    return (w, model) -> template.execute(w, model);
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of("i18n",
        (Function<String, String>) (key) -> loader.message(key));
  }
}
