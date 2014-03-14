package com.jeroenreijn.examples.viewresolvers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.TemplateLoader;
import com.samskivert.mustache.Template;

public class JmustacheViewResolver extends AbstractTemplateViewResolver {
  private static final Charset UTF8 = Charset.forName("utf8");
  private Compiler compiler;
  private TemplateLoader loader;

  @Override
  protected Class<?> requiredViewClass() {
    return JmustacheView.class;
  }

  @Override
  protected void initApplicationContext() {
    setViewClass(requiredViewClass());
    loader = new TemplateLoader() {
      public Reader getTemplate(String name) throws Exception {
        String url = getPrefix() + name + getSuffix();
        return new InputStreamReader(getApplicationContext().getResource(url)
            .getInputStream(), UTF8);
      }
    };
    compiler = Mustache.compiler().withLoader(loader);
    super.initApplicationContext();
  }

  @Override
  protected View loadView(String viewName, final Locale locale)
      throws Exception {
    JmustacheView v = (JmustacheView) super.loadView(viewName, locale);
    v.template = compiler.compile(loader.getTemplate(viewName));
    Mustache.Lambda i18n = new Mustache.Lambda() {
      public void execute(Template.Fragment frag, Writer out)
          throws IOException {
        final String key = frag.execute();
        final String text = getApplicationContext().getMessage(key, null,
            locale);
        out.write(text);
      }
    };
    v.addStaticAttribute("i18n", i18n);
    return v;
  }

  static class JmustacheView extends AbstractTemplateView {
    private Template template;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
      PrintWriter w = response.getWriter();
      try {
        w.write(template.execute(model));
      } finally {
        w.close();
      }
    }
  }
}
