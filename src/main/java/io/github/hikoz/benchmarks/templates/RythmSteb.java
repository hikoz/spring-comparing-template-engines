package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.rythmengine.extension.II18nMessageResolver;
import org.rythmengine.extension.ISourceCodeEnhancer;
import org.rythmengine.template.ITemplate;

import com.google.common.collect.ImmutableMap;
import com.jeroenreijn.examples.model.Presentation;

public class RythmSteb implements Steb {
  private RythmEngine engine;
  private StebLoader loader;

  public RythmSteb(StebLoader loader) throws IOException {
    this.loader = loader;
    Map<String, Object> conf = new HashMap<>();
    conf.put(RythmConfigurationKey.HOME_TEMPLATE.getKey(), loader.getTemplateDir());
    conf.put(RythmConfigurationKey.CODEGEN_SOURCE_CODE_ENHANCER.getKey(), new ISourceCodeEnhancer() {
      public List<String> imports() {
        return Arrays.asList(Presentation.class.getName());
      }

      public String sourceCode() {
        return null;
      }

      public Map<String, ?> getRenderArgDescriptions() {
        return new HashMap<>();
      }

      public void setRenderArgs(ITemplate template) {
      }
    });
    conf.put(RythmConfigurationKey.I18N_MESSAGE_RESOLVER.getKey(),
      (II18nMessageResolver)(t, key, args) -> loader.message(key, args));
    engine = new RythmEngine(conf);
    Files.walk(new File(loader.getTemplateDir()).toPath())
      .filter(p -> p.toString().endsWith(".html"))
      .forEach(p -> engine.getTemplate(p.toFile()));
  }

  @Override
  public StebTemplate template(String viewName) throws Exception {
    File file = new File(loader.getFilePath(viewName));
    // not work. engine.render(w, path, m);
    return (w, m) -> w.write(engine.render(file, m));
  }

  @Override
  public Map<String, ?> attributes() {
    return ImmutableMap.of();
  }
}
