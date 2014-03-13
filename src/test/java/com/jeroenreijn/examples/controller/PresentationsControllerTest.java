package com.jeroenreijn.examples.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.jeroenreijn.examples.PresentationsRepository;
import com.jeroenreijn.examples.model.Presentation;

public class PresentationsControllerTest {
  PresentationsController controller;

  @Before
  public void setUp() throws Exception {
    controller = new PresentationsController();
    controller.presentationsRepository = new PresentationsRepository() {
      public Presentation findPresentation(Long id) {
        return null;
      }

      public Iterable<Presentation> findAll() {
        return new ArrayList<Presentation>();
      }
    };
  }

  @Test
  public void should_return_jsp_view() throws Exception {
    final ModelAndView view = controller.home();
    assertThat(view.getViewName(), is("index-jsp"));
  }

  @Test
  public void should_return_other_view() throws Exception {
    final ModelAndView mv = controller.showList("test");
    assertThat(mv.getViewName(), is("index-test"));
  }

}
