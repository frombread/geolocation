package com.geolocation.country.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class main {

  @GetMapping("/")
  public String findIpForm() {
    return "main";
  }

}
