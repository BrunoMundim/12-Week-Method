package com.mundim.WeekMethod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("Home");
        return modelAndView;
    }

}
