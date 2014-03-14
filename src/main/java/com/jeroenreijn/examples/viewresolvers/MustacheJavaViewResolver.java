package com.jeroenreijn.examples.viewresolvers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Function;

public class MustacheJavaViewResolver extends AbstractTemplateViewResolver {
  protected static final Charset UTF8 = Charset.forName("utf8");
  private MustacheFactory compiler;

  public MustacheJavaViewResolver() {
    setViewClass(requiredViewClass());
  }

  @Override
  protected Class<?> requiredViewClass() {
    return MustacheJavaView.class;
  }

  @Override
  protected void initApplicationContext() {
    setViewClass(requiredViewClass());
    compiler = new DefaultMustacheFactory() {
      @Override
      public Reader getReader(String resourceName) {
        Resource r = getApplicationContext().getResource(
            getPrefix() + resourceName + getSuffix());
        try {
          return new InputStreamReader(r.getInputStream(), UTF8);
        } catch (IOException e) {
          throw new MustacheException("template load failed", e);
        }
      }
    };
    super.initApplicationContext();
  }

  @Override
  protected View loadView(String viewName, final Locale locale)
      throws Exception {
    MustacheJavaView v = (MustacheJavaView) super.loadView(viewName, locale);
    v.template = compiler.compile(viewName);
    Function<String, String> i18n = new Function<String, String>() {
      @Override
      public String apply(String in) {
        return getApplicationContext().getMessage(in, null, locale);
      }
    };
    v.addStaticAttribute("i18n", i18n);
    return v;
  }

  static class MustacheJavaView extends AbstractTemplateView {
    private Mustache template;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
        final HttpServletRequest request, HttpServletResponse response)
        throws Exception {
      PrintWriter w = response.getWriter();
      try {
        template.execute(w, model);
      } finally {
        w.close();
      }
    }
  }
}
