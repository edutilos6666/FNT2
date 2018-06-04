package com.edutilos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by edutilos on 02.06.18.
 */
@Controller
@RequestMapping("/")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        model.addAttribute("greeting", "Hello World from Spring 4 MVC");
        return "welcome";
    }

    @RequestMapping(value = "/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC");
        return "welcome";
    }

    @RequestMapping(value="/welcome2", method = RequestMethod.GET)
    public String welcome2(ModelMap model) {
        model.addAttribute("greeting", "Bonjour");
        model.addAttribute("name", "edutilos");
        model.addAttribute("age", 26);
        model.addAttribute("wage", 100.0);
        return "welcome2";
    }

}