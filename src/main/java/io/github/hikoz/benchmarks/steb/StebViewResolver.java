package io.github.hikoz.benchmarks.steb;

import io.github.hikoz.benchmarks.steb.Steb.StebTemplate;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class StebViewResolver extends AbstractTemplateViewResolver {
  private Steb steb;

  public StebViewResolver(Steb steb) {
    setViewClass(requiredViewClass());
    this.steb = steb;
  }

  @Override
  protected Class<?> requiredViewClass() {
    return StebView.class;
  }

  @Override
  protected void initApplicationContext() {
    setViewClass(requiredViewClass());
    setAttributesMap(steb.attributes());
    super.initApplicationContext();
  }

  @Override
  protected View loadView(String viewName, final Locale locale)
      throws Exception {
    StebView v = (StebView) super.loadView(viewName, locale);
    v.setContentType("text/html;charset=UTF-8");
    v.template = steb.template(viewName);
    return v;
  }

  static class StebView extends AbstractTemplateView {
    public StebTemplate template;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
        final HttpServletRequest request, HttpServletResponse response)
        throws Exception {
      try (PrintWriter w = response.getWriter()) {
        template.writeTo(w, model);
      }
    }
  }
}
