package com.jeroenreijn.examples.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class PresentationsControllerTest {
  PresentationsController controller;

  @Before
  public void setUp() throws Exception {
    controller = new PresentationsController();
    controller.presentationsRepository = () -> new ArrayList<>();
  }

  @Test
  public void should_return_jsp_view() throws Exception {
    final ModelAndView view = controller.home();
    assertThat(view.getViewName()).isEqualTo("index-velocity");
  }

  @Test
  public void should_return_other_view() throws Exception {
    final ModelAndView mv = controller.showList("test");
    assertThat(mv.getViewName()).isEqualTo("index-test");
  }
}
