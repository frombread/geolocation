package com.geolocation.country.service;

import java.net.UnknownHostException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindIpController {

  private final RedisService redisService;

  public FindIpController(RedisService redisService) {
    this.redisService = redisService;
  }

  @GetMapping("/find/{ip}")
  public String findIp(@PathVariable String ip) {
    System.out.println(ip);
    long LongKey = redisService.ipToLong(ip);
    return redisService.getCountriesInRangeByScore(LongKey);
  }
}
