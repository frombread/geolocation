package com.geolocation.country.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class SeedDataLoader implements CommandLineRunner {

  private final RedisService redisService;
  private final RedisTemplate<String, String> redisTemplate;

  public SeedDataLoader(RedisService redisService, RedisTemplate<String, String> redisTemplate) {
    this.redisService = redisService;
    this.redisTemplate = redisTemplate;
  }

  private static boolean isIPv6Address(String ipAddress) {
    return ipAddress.contains(":");
  }

  @Override
  public void run(String... args) throws Exception {
//    Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
//    File file = new File("/Users/bang-uhyeon/Downloads/country.csv");
//
//    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//      br.readLine();
//      String line;
//      while ((line = br.readLine()) != null) {
//        String[] columns = line.split(",");
//
//        if (columns.length >= 3) {
//          String startIp = columns[0].trim();
//
//          if (isIPv6Address(startIp)) {
//            System.out.println("IPv6 address skipped: " + startIp);
//            continue;
//          }
//
//          try {
//            long startIpScore = redisService.ipToLong(startIp);
//            redisService.addCountry(line, startIpScore);
//          } catch (NumberFormatException e) {
//            e.printStackTrace();
//          }
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    } finally {
//      System.err.println("저장 끝");
//    }
  }
}
