package com.geolocation.country;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


@Component
public class SeedDataLoader implements CommandLineRunner {

  private static long ipToLong(String ipAddress) {
    String[] ipAddressInArray = ipAddress.split("\\.");
    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {
      int power = 3 - i;
      result += (long) (Integer.parseInt(ipAddressInArray[i]) % 256 * Math.pow(256, power));
    }
    return result;
  }
  private static boolean isIPv6Address(String ipAddress) {
    // 간단한 IPv6 주소 패턴 체크
    return ipAddress.contains(":");
  }

  @Override
  public void run(String... args) throws Exception {
    File file = new File("/Users/bang-uhyeon/Downloads/country.csv");
    Jedis jedis = new Jedis("localhost", 6379);
    String sortedSetKey = "findIp";

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
            long startScore =ipToLong(startValue);
//            System.out.println("저장되는건가: " + startScore);
            jedis.zadd(sortedSetKey, startScore, memberValue);
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
      jedis.close();
    }
  }

}
