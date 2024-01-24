package com.geolocation.country.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;


@Component
public class SeedDataLoader implements CommandLineRunner {
  private final RedisService redisService;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  private  ZSetOperations<String, String> zSetOperations;
  public SeedDataLoader(RedisService redisService ){
    this.redisService = redisService;
  }
  private static boolean isIPv6Address(String ipAddress) {
    // 간단한 IPv6 주소 패턴 체크
    return ipAddress.contains(":");
  }

  @Override
  public void run(String... args) throws Exception {
    zSetOperations = redisTemplate.opsForZSet();

    String ip = "192.165.0.1";
    long start =redisService.ipToLong(ip);
    System.out.println(redisService.getCountriesInRangeByScore(start));
    long numberOfElements = zSetOperations.size("countries");

    System.out.println("Number of elements in 'countries': " + numberOfElements);
    // 데이터 검색
    Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
    File file = new File("/Users/bang-uhyeon/Downloads/country.csv");

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      br.readLine();
      String line;
      while ((line = br.readLine()) != null) {
        // CSV 파일에서 각 줄을 읽어오기
        String[] columns = line.split(",");

        if (columns.length >= 3) {
          String startValue = columns[0].trim(); // 1번째 열의 값 (start)
          String memberValue = columns[2].trim(); // 3번째 열의 값 (member)

          if (isIPv6Address(startValue)) {
            System.out.println("IPv6 address skipped: " + startValue);
            continue;
          }
          // start 값을 long으로 변환하여 Sorted Set에 추가
          try {
            long startScore = redisService.ipToLong(startValue);
            System.out.println("저장되는건가: " + startScore);
            redisService.addCountry(memberValue,startScore);
          } catch (NumberFormatException e) {
            // start 값을 long으로 변환할 수 없는 경우 처리
            System.err.println("Long으로 변환 못하는 애: " + startValue);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      System.err.println("저장 끝");
    }
  }
}
