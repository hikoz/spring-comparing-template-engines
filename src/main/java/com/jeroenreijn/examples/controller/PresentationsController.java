package com.jeroenreijn.examples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jeroenreijn.examples.PresentationsRepository;

@Controller
public class PresentationsController {

  @Autowired
  PresentationsRepository presentationsRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView home() {
    return showList("velocity");
  }

  @RequestMapping(value = "/{template}", method = RequestMethod.GET)
  public ModelAndView showList(@PathVariable(value = "template") final String template) {
    return new ModelAndView("index-" + template,
      "presentations", presentationsRepository.findAll());
  }

}