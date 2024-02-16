![스크린샷 2024-01-31 오후 9 24 25](https://github.com/frombread/geolocation/assets/120001069/f2f69e18-fe7d-43f3-b19d-94372d50b82c)

- ip 주소를 활용하는 방법을 직접 구현해보고 이해해보고자 프로젝트를 진행

# 접속방법

### [13.124.160.146](http://3.35.16.122/) 으로 접속

# **직접 실행 방법**

> **사전 작업**: redis 실행 (6379포트)

```bash
# 저장소를 복제합니다.
git clone https://github.com/frombread/geolocation
```

```bash
# 디렉토리로 이동
cd geolocation

# 자바 빌드
./gradlew clean bootJar

# 프로그램 실행
java -jar ./build/libs/country-0.0.1-SNAPSHOT.jar
```

```bash
http://localhost:8080으로 접속
```

# 다른 프론트와의 연동

```java
- Endpoint : "13.124.160.146/find/{ip}"
- Method: "GET"
- Response: "String" (특정 Ip 주소에 해당하는 나라 이름 반환)

`{ip}` 부분에 검사하고자 하는 IP 주소를 넣으면, 해당 IP가 위치한 나라의 이름을 영어로 받을 수 있습니다.
```
