# Living Progress - Build - Mentor Me API

Deployment Guide

### Description

Living Progress - Build - Mentor Me API.

## Prerequisites

1. Java 8
2. Maven3+
3. MySQL 5.7.x
4. SMTP Server You may use [FakeSMTP](https://github.com/Nilhcem/FakeSMTP)
5. Chrome with postman(to verify api only)

## Configuration


### application configuration
Edit `src/main/resources/application.properties`.
you must change **spring.datasource.url**, **spring.datasource.username**, **spring.datasource.password** to match your mysql configuration and 
**spring.mail.host**, **spring.mail.port**(more configurations please check commented email configurations for example auth,ttls and etc) to match your smtp configurations.
Others are recommend to not change but you may change with your need.
You may change port with key **server.port**, default is 8080.


### log4j configuration
Edit `src/main/resources/log4j.properties`.
You can change log level used in application with key **log4j.logger.com.livingprogress.mentorme**.

### email template configuration
Edit `src/main/resources/templates`.
It exists **subject.vm** and **body.vm** in nested folder with email name.

### test configuration
Edit `src/test/resources/test.properties`.
Same key defined `src/test/resources/test.properties` will overwrite configuration defined in `src/main/resources/application.properties`.
You must change **spring.datasource.url** if you want to test with different database defined in `src/main/resources/application.properties`.
You may change  **spring.mail.port ** to different port but please make sure not conflicts with ports in your computer it will start mock smtp server during test with this port.
so please not change **spring.mail.host** since it will listen **localhost**.


### custom configurations using command line parameters or system variables
Please check all property keys in `src/main/resources/application.properties`.
You can custom using command line parameters or system variables easily.
For example custom server port **-Dserver.port=8087** as command line parameter or using **set server.port=8087** under windows or **export SERVER_PORT=8087** under linux.
similar for database,email related configurations.


Details about order please check [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
For example if you define port in command line parameter and system variable at same time it will use command line parameter.


## Mysql setup 
Create schemas with `sqls/schema.sql`.
Create tables in above schemas with `sqls/ddl.sql`.
If you want to drop all tables please run `sqls/drop.sql`.
If you want to clean all tables please run `sqls/clear.sql`.
If you want to prepare test data please run `sqls/testdata.sql`.


## Running Tests
Make sure your configurations are right.

``` bash
mvn clean test
```

You can also test with coverage report in `target/site/jacoco/index.html` after run below command.

``` bash
mvn clean test jacoco:report
```

## Deployment
``` bash
mvn clean package
```
Move to `target` folder
Start api service listening `8080`
``` bash
java -jar mentorme-api.jar
```

## Swagger 
Open **http://editor.swagger.io/** and copy  `docs/swagger.yaml` to verify.

## Verification
Prepare clean and test data in mysql with `sqls/clear.sql` and `sqls/testdata.sql`.
Import Postman collection `docs/postman.json` with environment variables `docs/postman-env.json`.
You can test basic auth with username=test{X} X could be 1-14, password=password, please use basic auth feature of Postman to verify.
Almost all requests will use JWT token auth defined in environment variable, but you can change to use basic auth easily.
