package com.geolocation.country.service;

import java.net.UnknownHostException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindIpController {
    private final RedisService redisService;

    public FindIpController(RedisService redisService){
      this.redisService = redisService;
    }


    @GetMapping("/find/{key}")
    public String findIp(@PathVariable String key) {
      System.out.println(key);
      long LongKey = redisService.ipToLong(key);
      return redisService.getCountriesInRangeByScore(LongKey);
    }
}
