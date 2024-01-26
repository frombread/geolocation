package com.geolocation.country.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
    addCountryData();
  }

  @Scheduled(cron = "0 0 0 1/1 * ?")
  public void addCountryData() throws Exception {
    try {
      Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
      File file = downloadAndExtractFile();

      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
          String[] columns = line.split(",");

          if (columns.length >= 3) {
            String startIp = columns[0].trim();

            if (isIPv6Address(startIp)) {
              System.out.println("IPv6 address skipped: " + startIp);
              continue;
            }

            try {
              long startIpScore = redisService.ipToLong(startIp);
              redisService.addCountry(line, startIpScore);
            } catch (NumberFormatException e) {
              e.printStackTrace();
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        System.err.println("저장 끝_test");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

    private File downloadAndExtractFile () throws IOException {
      String fileUrl = "https://ipinfo.io/data/free/country.csv.gz?token=2c99cfcf976dce";

      // 파일을 다운로드 받아 임시 디렉토리에 저장
      Path tempFile = Files.createTempFile("location", ".csv.gz");
      URL url = new URL(fileUrl);

      try (InputStream in = url.openStream()) {
        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
      }

      // 압축 파일 해제
      Path csvFile = Files.createTempFile("location", ".csv");
      extractGzip(tempFile, csvFile);

      // 반환할 파일은 압축 해제된 CSV 파일
      return csvFile.toFile();
    }

    private void extractGzip (Path source, Path destination) throws IOException {
      try (InputStream inputStream = Files.newInputStream(
          source); GZIPInputStream gzipInputStream = new GZIPInputStream(
          inputStream); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(buffer)) > 0) {
          byteArrayOutputStream.write(buffer, 0, len);
        }

        Files.write(destination, byteArrayOutputStream.toByteArray(), StandardOpenOption.CREATE);
      }
    }

  }
