package com.jeroenreijn.examples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jeroenreijn.examples.PresentationsRepository;

@Controller
@RequestMapping("/")
public class PresentationsController {

  @Autowired
  PresentationsRepository presentationsRepository;

  @ResponseBody
  @RequestMapping(value = "str", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
  public String str() {
    return "<h1>こんにちは"
        + "<h3 class=\"panel-title\">Shootout! Template engines on the JVM - Jeroen Reijn</h3>";
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ModelAndView home() {
    return showList("velocity");
  }

  @RequestMapping(value = "{template}", method = RequestMethod.GET)
  public ModelAndView showList( @PathVariable(value = "template") final String template) {
    return new ModelAndView("index-" + template,
        "presentations", presentationsRepository.findAll());
  }

}