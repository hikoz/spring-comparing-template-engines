package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.I18n;
import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.JadeTemplate;
import de.neuland.jade4j.template.TemplateLoader;

public class JadeSteb implements Steb {
  private JadeConfiguration jade;

  public JadeSteb(StebLoader loader) {
    jade = new JadeConfiguration();
    jade.setCaching(true);
    jade.setPrettyPrint(false);
    jade.setTemplateLoader(new TemplateLoader() {
      public Reader getReader(String name) throws IOException {
        return loader.getReader(name);
      }

      public long getLastModified(String name) throws IOException {
        return loader.lastModified(name);
      }
    });
    jade.setSharedVariables(ImmutableMap.of("i18n", (I18n) (key) -> loader.message(key)));
  }

  @Override
  public StebTemplate template(String viewName) throws Exception {
    JadeTemplate template = jade.getTemplate(viewName);
    return (w, model) -> jade.renderTemplate(template, model, w);
  }

  @Override
  public Map<String, ?> attributes() {
    return ImmutableMap.of();
  }
}
