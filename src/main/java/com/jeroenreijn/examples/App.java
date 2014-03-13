package com.jeroenreijn.examples;

import httl.web.springmvc.HttlViewResolver;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.MustacheMessageInterceptor;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.mustache.MustacheTemplateLoader;
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

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan
public class App extends WebMvcConfigurerAdapter {
  private static final String UTF8 = "UTF-8";
  String CONTENT_TYPE = "text/html;charset=" + UTF8;

  @Bean
  public SpringTemplateLoader jade4jTemplateLoader() {
    SpringTemplateLoader l = new SpringTemplateLoader();
    l.setBasePath("/WEB-INF/jade/");
    return l;
  }

  @Bean
  public JadeConfiguration jadeConfiguration() {
    JadeConfiguration c = new JadeConfiguration();
    c.setPrettyPrint(false);
    c.setTemplateLoader(jade4jTemplateLoader());
    return c;
  }

  @Bean
  public JadeViewResolver jadeViewResolver() {
    JadeViewResolver r = new JadeViewResolver();
    r.setPrefix("/");
    r.setSuffix(".jade");
    r.setViewNames(new String[] { "*-jade" });
    r.setConfiguration(jadeConfiguration());
    r.setRenderExceptions(true);
    r.setContentType(CONTENT_TYPE);
    return r;
  }

  @Bean
  public MustacheViewResolver mustacheViewResolver() {
    MustacheViewResolver r = new MustacheViewResolver();
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
  public MustacheMessageInterceptor mustacheMessageInterceptor() {
    MustacheMessageInterceptor in = new MustacheMessageInterceptor();
    in.setMessageSource(messageSource());
    in.setLocaleResolver(new AcceptHeaderLocaleResolver());
    return in;
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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(mustacheMessageInterceptor());
    registry.addInterceptor(new HandlerInterceptorAdapter() {
      @Override
      public boolean preHandle(HttpServletRequest request,
          HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding(UTF8);
        return true;
      }
    });
  }

  private ServletContextTemplateResolver thymeleafTemplateResolver() {
    ServletContextTemplateResolver r = new ServletContextTemplateResolver();
    r.setPrefix("/WEB-INF/thymeleaf/");
    r.setSuffix(".html");
    r.setTemplateMode("HTML5");
    r.setCharacterEncoding(UTF8);
    return r;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine e = new SpringTemplateEngine();
    e.setTemplateResolver(thymeleafTemplateResolver());
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

  // FIXME NPE at org.fusesource.scalate.spring.view.ScalateUrlView.checkResource
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
