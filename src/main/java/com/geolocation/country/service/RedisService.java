package com.geolocation.country.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String data){
        // key는 키고, data는 data임
        ValueOperations<String,Object> values = redisTemplate.opsForValue();
        values.set(key,data);
    }
    public void setValues(String key, String data, Duration duration){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key,data,duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if(values.get(key)==null){
            return "false";
        }
        return (String) values.get(key);
    }
    public void deleteValues(String key){
        redisTemplate.delete(key);
    }

    public void expireValues(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
        //지정된 키의 값이 주저어진 시간 후에 만료 되도록 설정
    }

    // 지정된 키에 대한 해시 맵에 여러 데이터를 설정합니다.
    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.putAll(key, data);
    }

    // 지정된 키에 대한 해시 맵에서 주어진 해시키에 해당 하는 값을 가져옴?
    // 읽기전용 트랜잭션으로 설정
    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) redisTemplate.opsForHash().get(key, hashKey) : "";
    }
    // 지정된 키의 해시 맵에서 주어진 해시 키에 해당하는 값을 삭제
    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }
    // 주어진 값이 "false"와 같지 않은지 확인합니다.
    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}
