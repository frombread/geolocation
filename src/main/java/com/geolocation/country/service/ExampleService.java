//package com.geolocation.country.service;
//
//import java.time.Duration;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ExampleService {
//
//  private final RedisService redisService;
//
//  public ExampleService(RedisService redisService) {
//    this.redisService = redisService;
//  }
//
//  @PostMapping("/redisTest")
//  public String exampleUsage() {
//    // 데이터 저장
////    redisService.addCountry("192.168.0.3", "썽균");
//    return "Success";
//  }
//
//  @GetMapping("/redisTest/{key}")
//  public String test(){
//    String country = redisService.getCountry("k");
//    System.out.println("Country for IP 192.168.0.1: " + country);
//    return country;
//  }
//}
