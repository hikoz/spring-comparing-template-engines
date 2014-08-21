package io.github.hikoz.benchmarks.templates;

import io.github.hikoz.benchmarks.steb.Steb;
import io.github.hikoz.benchmarks.steb.StebLoader;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.TemplateLoader;
import com.samskivert.mustache.Template;

public class JmustacheSteb implements Steb {
  private TemplateLoader templateLoader;
  private Compiler compiler;
  private StebLoader loader;

  public JmustacheSteb(StebLoader loader) {
    this.loader = loader;
    this.templateLoader = name -> loader.getReader(name);
    compiler = Mustache.compiler().withLoader(this.templateLoader);
  }

  public StebTemplate template(String viewName) throws Exception {
    Template template = compiler.compile(templateLoader.getTemplate(viewName));
    return (w, model) -> w.write(template.execute(model));
  }

  public Map<String, ?> attributes() {
    return ImmutableMap.of("i18n",
        (Mustache.Lambda) (frag, out) -> out.write(loader.message(frag.execute())));
  }
}
