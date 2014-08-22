package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.helper.I18nHelper;
import com.github.jknack.handlebars.helper.I18nSource;
import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;
import com.google.common.collect.ImmutableMap;

public class HandlebarsSteb implements Steb {
  private Handlebars handlebars;

  public HandlebarsSteb(StebLoader loader) {
    handlebars = new Handlebars(new AbstractTemplateLoader() {
      public TemplateSource sourceAt(String location) throws IOException {
        return new URLTemplateSource(location, loader.getURL(location));
      }
    })
        .with(new HighConcurrencyTemplateCache());
    I18nHelper.i18n.setSource(new I18nSource() {
      public String message(String key, Locale locale, Object... args) {
        return loader.message(key, args);
      }

      public String[] keys(String baseName, Locale locale) {
        throw new UnsupportedOperationException();
      }
    });
  }

  public StebTemplate template(String viewName) throws Exception {
    Template template = handlebars.compile(viewName);
    return (w, model) -> {
      try {
        template.apply(model, w);
      } catch (Exception e) {
        throw new RuntimeException("failed to render:" + viewName, e);
      }
    };
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of();
  }
}
