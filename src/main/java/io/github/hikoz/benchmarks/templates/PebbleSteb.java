package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class PebbleSteb implements Steb {
  private PebbleEngine engine;

  public PebbleSteb(StebLoader loader) {
    engine = new PebbleEngine(new FileLoader() {
      public Reader getReader(String templateName) throws LoaderException {
        return loader.getReader(templateName);
      }
    });
//    engine.setTemplateCache(CacheBuilder.newBuilder().maximumSize(50).build());
    engine.addExtension(new AbstractExtension() {
      public Map<String, Function> getFunctions() {
        Map<String, Function> functions = new HashMap<>();
        functions.put("i18n", new Function() {
          public List<String> getArgumentNames() {
            return Arrays.asList("key");
          }

          public Object execute(Map<String, Object> args) {
            return loader.message((String) args.get("key"));
          }
        });
        return functions;
      }
    });
  }

  public StebTemplate template(String viewName) throws Exception {
    PebbleTemplate template = engine.getTemplate(viewName);
    return (w, model) -> {
      try {
        template.evaluate(w, model);
      } catch (Exception e) {
        throw new RuntimeException("failed to render template:" + viewName, e);
      }
    };
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of();
  }
}
