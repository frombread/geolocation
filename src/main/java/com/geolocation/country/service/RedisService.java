package com.geolocation.country.service;

import java.util.Objects;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


@Service
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;
  private final ZSetOperations<String, String> zSetOperations;

  public RedisService(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.zSetOperations = redisTemplate.opsForZSet();
  }

  public void addCountry(String country, double score) {
    zSetOperations.add("countries", country, score);
  }
  public String getCountriesInRangeByScore(long minScore) {
    System.out.println(minScore);
//    long max_score = ipToLong("255.255.255.255");
    Set<String> countries = zSetOperations.reverseRangeByScore("countries",-1 , minScore,0,2);
    System.out.println(countries);
    return Objects.requireNonNull(countries).isEmpty() ? null : countries.iterator().next();

  }
  public long ipToLong(String ipAddress) {
    String[] ipAddressInArray = ipAddress.split("\\.");
    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {
      int power = 3 - i;
      result += (long) (Integer.parseInt(ipAddressInArray[i]) % 256 * Math.pow(256, power));
    }
    return result;
  }


}
