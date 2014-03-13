package com.jeroenreijn.examples;

import httl.web.springmvc.HttlViewResolver;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.scalate.spring.view.ScalateViewResolver;
import org.rythmengine.spring.web.RythmConfigurer;
import org.rythmengine.spring.web.RythmViewResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.mustache.MustacheTemplateLoader;
import org.springframework.web.servlet.view.mustache.MustacheView;
import org.springframework.web.servlet.view.mustache.MustacheViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.URLTemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan
public class App {
  private static final String UTF8 = "UTF-8";
  String CONTENT_TYPE = "text/html;charset=" + UTF8;

  @Bean
  public SpringTemplateLoader jade4jTemplateLoader() {
    SpringTemplateLoader l = new SpringTemplateLoader();
    l.setBasePath("/WEB-INF/jade/");
    return l;
  }

  @Bean
  public JadeViewResolver jadeViewResolver() {
    JadeViewResolver r = new JadeViewResolver();
    r.setPrefix("/");
    r.setSuffix(".jade");
    r.setViewNames(new String[] { "*-jade" });
    JadeConfiguration c = new JadeConfiguration();
    c.setPrettyPrint(false);
    c.setTemplateLoader(jade4jTemplateLoader());
    r.setConfiguration(c);
    r.setRenderExceptions(true);
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  // TODO add i18n to model without creating sub class of View
  public static class MustacheSubView extends MustacheView {
    protected MessageSource messageSource;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
        final HttpServletRequest request, HttpServletResponse response)
        throws Exception {
      model.put("i18n", new Mustache.Lambda() {
        public void execute(Template.Fragment frag, Writer out)
            throws IOException {
          final String key = frag.execute();
          final String text = messageSource.getMessage(key, null,
              request.getLocale());
          out.write(text);
        }
      });
      super.renderMergedTemplateModel(model, request, response);
    }
  }

  @Bean
  public MustacheViewResolver mustacheViewResolver() {
    final MessageSource messageSource = messageSource();
    MustacheViewResolver r = new MustacheViewResolver() {
      @Override
      protected Class<?> getViewClass() {
        return MustacheSubView.class;
      }

      @Override
      protected View loadView(String viewName, Locale locale) throws Exception {
        MustacheSubView v = (MustacheSubView) super.loadView(viewName, locale);
        v.messageSource = messageSource;
        return v;
      }
    };
    r.setPrefix("/WEB-INF/mustache/");
    r.setSuffix(".mustache");
    r.setViewNames(new String[] { "*-mustache" });
    r.setTemplateLoader(mustacheTemplateLoader());
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public MustacheTemplateLoader mustacheTemplateLoader() {
    return new MustacheTemplateLoader();
  }

  @Bean
  public HandlebarsViewResolver handlebarsViewResolver() {
    HandlebarsViewResolver r = new HandlebarsViewResolver() {
      @Override
      protected Handlebars createHandlebars(URLTemplateLoader templateLoader) {
        return super.createHandlebars(templateLoader).with(
            new HighConcurrencyTemplateCache());
      }
    };
    r.setPrefix("/WEB-INF/handlebars/");
    r.setSuffix(".hbs");
    r.setBindI18nToMessageSource(true);
    r.setViewNames(new String[] { "*-handlebars" });
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine e = new SpringTemplateEngine();
    ServletContextTemplateResolver r = new ServletContextTemplateResolver();
    r.setPrefix("/WEB-INF/thymeleaf/");
    r.setSuffix(".html");
    r.setTemplateMode("HTML5");
    r.setCharacterEncoding(UTF8);
    e.setTemplateResolver(r);
    return e;
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver r = new ThymeleafViewResolver();
    r.setViewNames(new String[] { "*-thymeleaf" });
    r.setTemplateEngine(templateEngine());
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public FreeMarkerConfigurer freeMarkerConfigurer() {
    FreeMarkerConfigurer c = new FreeMarkerConfigurer();
    c.setTemplateLoaderPath("/WEB-INF/freemarker");
    c.setDefaultEncoding(UTF8);
    Properties p = new Properties();
    p.setProperty("auto_import", "spring.ftl as spring");
    c.setFreemarkerSettings(p);
    return c;
  }

  @Bean
  public FreeMarkerViewResolver freeMarkerViewResolver() {
    FreeMarkerViewResolver r = new FreeMarkerViewResolver();
    r.setViewNames(new String[] { "*-freemarker" });
    r.setSuffix(".ftl");
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public VelocityConfigurer velocityConfigurer() {
    VelocityConfigurer c = new VelocityConfigurer();
    c.setResourceLoaderPath("/WEB-INF/velocity/");
    Properties p = new Properties();
    p.setProperty("velocimacro.library", "includes.vm");
    c.setVelocityProperties(p);
    return c;
  }

  @Bean
  public VelocityViewResolver velocityViewResolver() {
    VelocityViewResolver r = new VelocityViewResolver();
    r.setViewNames(new String[] { "*-velocity" });
    r.setSuffix(".vm");
    r.setExposeSpringMacroHelpers(true);
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  // FIXME NPE at
  // org.fusesource.scalate.spring.view.ScalateUrlView.checkResource
  // @Bean
  public ScalateViewResolver scalateViewResolver() {
    ScalateViewResolver r = new ScalateViewResolver();
    r.setPrefix("/WEB-INF/scalate/");
    r.setSuffix(".scaml");
    return r;
  }

  @Bean
  public RythmConfigurer rythmConfigurer() {
    RythmConfigurer c = new RythmConfigurer();
    c.setDevMode(false);
    c.setResourceLoaderPath("/WEB-INF/rythm/");
    c.setAutoImports("com.jeroenreijn.examples.model.*");
    return c;
  }

  @Bean
  public RythmViewResolver rythmViewResolver() {
    RythmViewResolver r = new RythmViewResolver();
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public HttlViewResolver httlViewResolver() {
    HttlViewResolver r = new HttlViewResolver();
    r.setPrefix("/WEB-INF/httl/");
    r.setSuffix(".httl");
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource s = new ResourceBundleMessageSource();
    s.setBasename("mymessages");
    s.setDefaultEncoding(UTF8);
    return s;
  }
}