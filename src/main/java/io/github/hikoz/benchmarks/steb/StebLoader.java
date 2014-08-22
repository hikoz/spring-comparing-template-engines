package io.github.hikoz.benchmarks.steb;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.base.Charsets;

public class StebLoader {
  Charset UTF8 = Charsets.UTF_8;
  private final WebApplicationContext context;
  private final String prefix;
  private final String suffix;

  public StebLoader(WebApplicationContext context, String path) {
    String[] split = path.split("\\*");
    this.context = context;
    this.prefix = split[0];
    this.suffix = split[1];
  }

  private Resource getResource(String viewName) {
    return context.getResource(prefix + viewName.replace("-steb", "") + suffix);
  }

  public Reader getReader(String viewName) {
    try {
      return new InputStreamReader(getResource(viewName).getInputStream(), UTF8);
    } catch (IOException e) {
      throw new RuntimeException("fail to load resource:" + viewName, e);
    }
  }

  public String getFilePath(String viewName) {
    try {
      return getResource(viewName).getFile().getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException("fail to load resource:" + viewName, e);
    }
  }

  public long lastModified(String name) throws IOException {
    return getResource(name).lastModified();
  }

  public String message(String key, Object... args) {
    return context.getMessage(key, args, LocaleContextHolder.getLocale());
  }

  public String getTemplateDir() {
    try {
      return context.getResource(prefix).getFile().getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException("fail to load resource:" + prefix, e);
    }
  }

  public URL getURL(String location) {
    try {
      return getResource(location).getURL();
    } catch (IOException e) {
      throw new RuntimeException("fail to load resource:" + location, e);
    }
  }
}
