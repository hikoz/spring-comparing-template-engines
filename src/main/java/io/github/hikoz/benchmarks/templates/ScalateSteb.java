package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.File;
import java.util.Map;

import org.fusesource.scalate.TemplateEngine;

import scala.collection.JavaConverters;
import scala.collection.Traversable;

import com.google.common.collect.ImmutableMap;

public class ScalateSteb implements Steb {
  private TemplateEngine templateEngine;
  private StebLoader loader;

  @SuppressWarnings("all")
  public ScalateSteb(StebLoader loader) {
    this.loader = loader;
    Traversable<File> none = (Traversable<File>)
        scala.collection.Iterable$.MODULE$.empty();
    this.templateEngine = new TemplateEngine(none, "production");
  }

  public StebTemplate template(String viewName) {
    String file = loader.getFilePath(viewName);
    return (w, model) -> templateEngine.layout(file, w, asScala(model));
  }

  private static scala.collection.immutable.Map<String, Object> asScala(Map<String, Object> model) {
    return JavaConverters.mapAsScalaMapConverter(model).asScala()
        .toMap(scala.Predef.<scala.Tuple2<String, Object>> conforms());
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of("i18n", new I18n());
  }

  // type is required for Scalate
  public class I18n {
    public String get(String key) {
      return loader.message(key);
    }
  }
}
