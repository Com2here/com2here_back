1. 클론할 폴더 생성
2. bash로 생성한 폴더로 이동 후 git clone giturl
3. com2here_back\src\main\resources 에 application.properties 생성

application.properties :
```
# Spring Application Name
spring.application.name=com2hereback

spring.config.import=optional:file:../../.env[.properties]
# 데이터베이스 설정
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 설정
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

# JWT 설정
jwt.secret=${JWT_SECRET}
jwt.access-token-expiration-time=${JWT_ACCESS_TOKEN_EXPIRATION_TIME}
jwt.refresh-token-expiration-time=${JWT_REFRESH_TOKEN_EXPIRATION_TIME}

# 이메일 설정
spring.mail.host=${SPRING_MAIL_HOST}
spring.mail.port=${SPRING_MAIL_PORT}
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.auth-code-expiration-millis=1800000  # 30분

# Redis 설정
spring.data.redis.host=${SPRING_DATA_REDIS_HOST}
spring.data.redis.port=${SPRING_DATA_REDIS_PORT}
spring.data.redis.password=${SPRING_DATA_REDIS_PASSWORD}

spring.session.store-type=

server.servlet.session.timeout=
spring.session.redis.namespace=

# Server Configuration
server.port=3000

# Kakao Configuration
kakao.authUrl=
kakao.userApiUrl=
kakao.restapiKey=${KAKAO_RESTAPI_KEY}
# 리다이렉트될 URL
kakao.redirectUrl=

# Google Configuration
google.authUrl=
google.tokenUrl=
google.userApiUrl=
google.restapiKey=${GOOGLE_RESTAPI_KEY}
# 리다이렉트될 URL
google.redirectUrl=
google.client-secret=${GOOGLE_CLIENT_SECRET}
google.scope=

# Naver Configuration
naver.authUrl=
naver.tokenUrl=
naver.userApiUrl=
naver.restapiKey=${NAVER_RESTAPI_KEY}
naver.client-secret=${NAVER_CLIENT_SECRET}
# 리다이렉트될 URL
naver.redirectUrl=

# Logging Configuration
logging.level.root=INFO
# logging.level.com.com2here.com2hereback=DEBUG

spring.main.allow-bean-definition-overriding=true

```
src와 동일한 레벨에서 .env 파일 생성
.env :
```
# DB 설정
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

# JWT 설정
JWT_SECRET=
JWT_ACCESS_TOKEN_EXPIRATION_TIME=
JWT_REFRESH_TOKEN_EXPIRATION_TIME=

# MAIL 설정
SPRING_MAIL_HOST=
SPRING_MAIL_PORT=
SPRING_MAIL_USERNAME=
SPRING_MAIL_PASSWORD=

# Redis 설정
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379
SPRING_DATA_REDIS_PASSWORD=

# 소셜 KEY
KAKAO_RESTAPI_KEY=
GOOGLE_RESTAPI_KEY=
GOOGLE_CLIENT_SECRET=
NAVER_RESTAPI_KEY=
NAVER_CLIENT_SECRET=
```
