package com.jeroenreijn.examples;

import httl.web.springmvc.HttlViewResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fusesource.scalate.spring.view.ScalateViewResolver;
import org.rythmengine.spring.web.RythmConfigurer;
import org.rythmengine.spring.web.RythmViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.URLTemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.jeroenreijn.examples.viewresolvers.JmustacheViewResolver;
import com.jeroenreijn.examples.viewresolvers.MustacheJavaViewResolver;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.spring.PebbleTemplateLoader;
import com.mitchellbosecke.pebble.spring.PebbleViewResolver;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeView;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan
public class App {
  private static final String UTF8 = "UTF-8";
  String CONTENT_TYPE = "text/html;charset=" + UTF8;
  @Autowired
  WebApplicationContext ac;

  @Bean
  public JadeViewResolver jadeViewResolver() {
    JadeViewResolver r = new JadeViewResolver() {
      protected View loadView(String viewName, final Locale locale)
          throws Exception {
        JadeView v = (JadeView) super.loadView(viewName, locale);
        v.addStaticAttribute("i18n", new I18nHelper(locale));
        return v;
      }
    };
    r.setPrefix("/WEB-INF/jade/");
    r.setSuffix(".jade");
    r.setViewNames(new String[] { "*-jade" });
    JadeConfiguration c = new JadeConfiguration();
    c.setPrettyPrint(false);
    SpringTemplateLoader l = new SpringTemplateLoader();
    l.setServletContext(ac.getServletContext());
    l.init();
    c.setTemplateLoader(l);
    r.setConfiguration(c);
    r.setRenderExceptions(true);
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public MustacheJavaViewResolver mustacheJavaViewResolver() {
    MustacheJavaViewResolver r = new MustacheJavaViewResolver();
    r.setPrefix("/WEB-INF/mustache/");
    r.setSuffix(".mustache");
    r.setViewNames(new String[] { "*-mustache" });
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public JmustacheViewResolver jmustacheViewResolver() {
    JmustacheViewResolver r = new JmustacheViewResolver();
    r.setPrefix("/WEB-INF/jmustache/");
    r.setSuffix(".mustache");
    r.setViewNames(new String[] { "*-jmustache" });
    r.setContentType(CONTENT_TYPE);
    return r;
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
  public ThymeleafViewResolver thymeleafViewResolver() throws Exception {
    ThymeleafViewResolver r = new ThymeleafViewResolver();
    r.setViewNames(new String[] { "*-thymeleaf" });
    SpringTemplateEngine e = new SpringTemplateEngine();
    ServletContextTemplateResolver tr = new ServletContextTemplateResolver();
    tr.setPrefix("/WEB-INF/thymeleaf/");
    tr.setSuffix(".html");
    tr.setTemplateMode("HTML5");
    tr.setCharacterEncoding(UTF8);
    e.setTemplateResolver(tr);
    e.setMessageSource(messageSource());
    e.afterPropertiesSet();
    r.setTemplateEngine(e);
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
  @Bean
  public ScalateViewResolver scalateViewResolver() {
    ScalateViewResolver r = new ScalateViewResolver();
    r.setPrefix("/WEB-INF/scalate/");
    r.setSuffix(".scaml");
    r.setViewNames(new String[] { "*-scalate" });
    return r;
  }

  @Bean
  public RythmConfigurer rythmConfigurer() {
    RythmConfigurer c = new RythmConfigurer() {
      // required from spring-4.0.3
      public void configurePathMatch(PathMatchConfigurer configurer) {
        return;
      }
    };
    c.setDevMode(false);
    c.setResourceLoaderPath("/WEB-INF/rythm/");
    c.setAutoImports("com.jeroenreijn.examples.model.*");
    return c;
  }

  @Bean
  public RythmViewResolver rythmViewResolver() {
    RythmViewResolver r = new RythmViewResolver();
    r.setContentType(CONTENT_TYPE);
    r.setViewNames(new String[] { "*-rythm" });
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

  @Bean
  public ViewResolver stringViewResolver() {
    UrlBasedViewResolver r = new UrlBasedViewResolver();
    r.setViewClass(StringView.class);
    r.setViewNames(new String[] { "*-string" });
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public JtwigViewResolver jtwigViewResolver() {
    JtwigViewResolver r = new JtwigViewResolver();
    r.setPrefix("/WEB-INF/jtwig/");
    r.setSuffix(".twig");
    r.setContentType(CONTENT_TYPE);
    r.setViewNames(new String[] { "*-jtwig" });
    r.setCached(true);
    return r;
  }

  @Bean
  public PebbleViewResolver pebbleViewResolver() {
    PebbleTemplateLoader l = new PebbleTemplateLoader();
    l.setResourceLoader(ac);
    PebbleEngine engine = new PebbleEngine(l);
    engine.addExtension(new AbstractExtension() {
      public Map<String, Function> getFunctions() {
        Map<String, Function> functions = new HashMap<>();
        functions.put("i18n", new Function() {
          public List<String> getArgumentNames() {
            return Arrays.asList("key");
          }

          public Object execute(Map<String, Object> args) {
            return ac.getMessage((String) args.get("key"), null, LocaleContextHolder.getLocale());
          }
        });
        return functions;
      }
    });
    PebbleViewResolver r = new PebbleViewResolver();
    r.setPrefix("/WEB-INF/pebble/");
    r.setSuffix(".pebble");
    r.setContentType(CONTENT_TYPE);
    r.setPebbleEngine(engine);
    r.setViewNames(new String[] { "*-pebble" });
    return r;
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new AcceptHeaderLocaleResolver();
  }

  public final class I18nHelper {
    private final Locale locale;

    private I18nHelper(Locale locale) {
      this.locale = locale;
    }

    public CharSequence message(String key) {
      return ac.getMessage(key, null, locale);
    }
  }

  static class StringView extends AbstractTemplateView {
    protected void renderMergedTemplateModel(Map<String, Object> model,
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      String s = "<h1>こんにちは"
          + "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
      response.getWriter().write(s);
    }
  }
}
