package io.github.hikoz.benchmarks.spring;

import httl.web.springmvc.HttlViewResolver;
import io.github.hikoz.benchmarks.steb.I18n;
import io.github.hikoz.benchmarks.steb.StebLoader;
import io.github.hikoz.benchmarks.steb.StebViewResolver;
import io.github.hikoz.benchmarks.templates.HandlebarsSteb;
import io.github.hikoz.benchmarks.templates.JadeSteb;
import io.github.hikoz.benchmarks.templates.JmustacheSteb;
import io.github.hikoz.benchmarks.templates.MustacheJavaSteb;
import io.github.hikoz.benchmarks.templates.PebbleSteb;
import io.github.hikoz.benchmarks.templates.RythmSteb;
import io.github.hikoz.benchmarks.templates.ScalateSteb;
import io.github.hikoz.benchmarks.templates.StringSteb;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.rythmengine.spring.web.RythmConfigurer;
import org.rythmengine.spring.web.RythmViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
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
import com.jeroenreijn.examples.InMemoryPresentationsRepository;
import com.jeroenreijn.examples.PresentationsRepository;
import com.jeroenreijn.examples.controller.PresentationsController;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.spring.PebbleTemplateLoader;
import com.mitchellbosecke.pebble.spring.PebbleViewResolver;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@Configuration
public class App {
  private static final String UTF8 = "UTF-8";
  String CONTENT_TYPE = "text/html;charset=" + UTF8;
  @Autowired
  WebApplicationContext ac;

  @Bean
  public JadeViewResolver jadeViewResolver() {
    JadeViewResolver r = new JadeViewResolver();
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
    r.getAttributesMap().put("i18n",
        (I18n) (key) -> ac.getMessage(key, null, LocaleContextHolder.getLocale()));
    return r;
  }

  @Bean
  public StebViewResolver jadeViewResolverSteb() {
    StebViewResolver r = new StebViewResolver(new JadeSteb(
        new StebLoader(ac, "/WEB-INF/jade/*.jade")));
    r.setViewNames(new String[] { "*-jade-steb" });
    return r;
  }

  @Bean
  public StebViewResolver mustacheJavaViewResolver() {
    StebViewResolver r = new StebViewResolver(new MustacheJavaSteb(
        new StebLoader(ac, "/WEB-INF/mustache/*.mustache")));
    r.setViewNames(new String[] { "*-mustache" });
    return r;
  }

  @Bean
  public StebViewResolver jmustacheViewResolver() {
    StebViewResolver r = new StebViewResolver(new JmustacheSteb(
        new StebLoader(ac, "/WEB-INF/jmustache/*.mustache")));
    r.setViewNames(new String[] { "*-jmustache" });
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
  public ViewResolver handlebarsViewResolverSteb() {
    StebLoader stebLoader = new StebLoader(ac, "/WEB-INF/handlebars/*.hbs");
    StebViewResolver r = new StebViewResolver(new HandlebarsSteb(stebLoader));
    r.setViewNames(new String[] { "*-handlebars-steb" });
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

  @Bean
  public StebViewResolver scalateViewResolverSteb() {
    StebLoader stebLoader = new StebLoader(ac, "/WEB-INF/scalate/*.scaml");
    StebViewResolver r = new StebViewResolver(new ScalateSteb(stebLoader));
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
  public StebViewResolver rythmViewResolverSteb() throws IOException {
    StebLoader stebLoader = new StebLoader(ac, "/WEB-INF/rythm/*.html");
    StebViewResolver r = new StebViewResolver(new RythmSteb(stebLoader));
    r.setViewNames(new String[] { "*-rythm-steb" });
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
  public StebViewResolver stringViewResolverSteb() {
    StebLoader stebLoader = new StebLoader(ac, "/WEB-INF/string/*.txt");
    StebViewResolver r = new StebViewResolver(new StringSteb(stebLoader));
    r.setViewNames(new String[] { "*-string" });
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
  public StebViewResolver pebbleViewResolverSteb() {
    StebLoader stebLoader = new StebLoader(ac, "/WEB-INF/pebble/*.pebble");
    StebViewResolver r = new StebViewResolver(new PebbleSteb(stebLoader));
    r.setViewNames(new String[] { "*-pebble-steb" });
    return r;
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new AcceptHeaderLocaleResolver();
  }

  @Bean
  public PresentationsRepository presentationsRepository() {
    return new InMemoryPresentationsRepository();
  }

  @Bean
  PresentationsController presentationsController() {
    return new PresentationsController();
  }
}
